import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecException
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PGobject
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.URI
import java.sql.Connection
import java.sql.SQLException


object Postgres {
    const val SUPER_USER = "postgres"
    const val ENV_PASSWORD = "PGPASSWORD"

    data class PgDb(
      val host: String,
      val port: Int,
      val db: String,
      val user: String,
      val password: String)

    val pgParams: PgDb by lazy {

        val pgUrl: String = System.getenv("JDBC_DATABASE_URL").let {
            if (it == null || it.isEmpty()) {
                println("no JDBC_DATABASE_URL environment variable found, using default value")
                "jdbc:postgresql://localhost:5432/ion?user=postgres&password=changeit"
            } else {
                it
            }
        }

        val pgUri = URI(pgUrl.substring(5))
        val pgMap = pgUri.query.split('&')
          .map { it.split('=') }
          .map { it[0] to it[1] }
          .toMap()

        val prms = PgDb(pgUri.host, pgUri.port.toInt(), pgUri.path.substring(1),
          pgMap["user"] ?: error("missing user on "),
          pgMap["password"] ?: error("missing password"))

        println("Using DB: $prms")

        prms
    }
}

object Docker {
    const val IMAGE_NAME = "postgres"
    const val CONTAINER_NAME = "pg-container"
    const val CONTAINER_PORT = 5432
    const val HOST_MOUNT_DIR = "src/test/resources/docker"
    const val CONTAINER_MOUNT_DIR = "/mnt"

    fun isContainerRunning(project: Project): Boolean {
        val pipe = ByteArrayOutputStream()
        project.exec {
            commandLine("docker", "ps",
              "-a",
              "-q",
              "-f", "name=$CONTAINER_NAME")

            standardOutput = pipe
        }

        // the pipe will be empty if the container does not exist
        return pipe.size() > 0
    }

    fun tryConnectDb(project: Project): Boolean = try {
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "psql",
              "-h", Postgres.pgParams.host,
              "-U", Postgres.SUPER_USER,
              "-w",
              "-1",
              "-c", "select")

            // sink
            errorOutput = DevNull()
            standardOutput = DevNull()
        }
        true
    } catch (e: ExecException) {
        false
    }
}

/// Docker related Tasks
open class PgStart : AbstractTask() {
    @TaskAction
    fun run() {
        if (Docker.isContainerRunning(project)) {
            println("Container \"${Docker.CONTAINER_NAME}\" already exists. Skipping \"docker run\"...")
            return
        }

        println("Mounted host's directory \"${project.buildDir.absolutePath}/${Docker.HOST_MOUNT_DIR}\" " +
          "on \"${Docker.CONTAINER_MOUNT_DIR}\"")

        project.exec {
            commandLine("docker", "run",
              "--name", Docker.CONTAINER_NAME,
              "-e", "POSTGRES_PASSWORD=${Postgres.pgParams.password}",
              "-p", "${Postgres.pgParams.port}:${Docker.CONTAINER_PORT}/tcp",
              "-v", "${project.rootDir.absolutePath}/${Docker.HOST_MOUNT_DIR}:${Docker.CONTAINER_MOUNT_DIR}",
              "-d", Docker.IMAGE_NAME)

            standardOutput = DevNull()
        }

        val tick = 250L
        while (!Docker.tryConnectDb(project)) {
            println("Polling for DBMS availability...")
            Thread.sleep(tick)
        }
    }
}

open class PgStop : AbstractTask() {
    @TaskAction
    fun run() {
        if (Docker.isContainerRunning(project)) {
            println("Removing container \"${Docker.CONTAINER_NAME}\"")
            project.exec {
                commandLine("docker", "rm", "--force", Docker.CONTAINER_NAME)
                standardOutput = DevNull()
            }
            return
        }
        println("Container \"${Docker.CONTAINER_NAME}\" does not exist. Skipping \"docker rm\"...")
    }
}

/**
 * Removes the container if it is running.
 * Otherwise, creates the container.
 */
open class PgToggle : AbstractTask() {
    @TaskAction
    fun run() {
        if (Docker.isContainerRunning(project)) {
            println("Detected running container \"${Docker.CONTAINER_NAME}\". Removing...")
            project.exec {
                commandLine("docker", "rm", "--force", Docker.CONTAINER_NAME)
                standardOutput = DevNull()
            }
            return
        }
        println("Container \"${Docker.CONTAINER_NAME}\" not running. Creating...")
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "run",
              "--name", Docker.CONTAINER_NAME,
              "-e", "POSTGRES_PASSWORD=${pgParams.password}",
              "-p", "${pgParams.port}:${Docker.CONTAINER_PORT}/tcp",
              "-v", "${project.rootDir.absolutePath}/${Docker.HOST_MOUNT_DIR}" +
              ":${Docker.CONTAINER_MOUNT_DIR}",
              "-d", Docker.IMAGE_NAME)

            standardOutput = DevNull()
        }
    }
}

/// PostgreSQL related Tasks
open class PgCreateUser : AbstractTask() {
    @TaskAction
    fun run() {
        val pgParams = Postgres.pgParams
        if (pgParams.user == Postgres.SUPER_USER) {
            return
        }
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "createuser",
              "-d",
              "-U", Postgres.SUPER_USER,
              "-h", pgParams.host,
              "-w", pgParams.user)
        }

        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "psql",
              "-d", Postgres.SUPER_USER,
              "-U", Postgres.SUPER_USER,
              "-h", pgParams.host,
              "-w",
              "-c", "ALTER USER ${pgParams.user} WITH PASSWORD '${pgParams.password}';")

            environment(Postgres.ENV_PASSWORD, pgParams.password)
        }
    }
}

open class PgDropDb : AbstractTask() {
    @TaskAction
    fun run() {
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "dropdb", "--if-exists",
              "-h", pgParams.host,
              "-U", pgParams.user,
              "-w", pgParams.db)

            environment(Postgres.ENV_PASSWORD, pgParams.password)
        }
    }
}

open class PgCreateDb : AbstractTask() {
    @TaskAction
    fun run() {
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "createdb",
              "-h", pgParams.host,
              "-U", pgParams.user,
              "-w", pgParams.db)

            environment(Postgres.ENV_PASSWORD, pgParams.password)
        }
    }
}

open class PgInitSchema : AbstractTask() {
    @TaskAction
    fun run() {
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "psql",
              "-h", pgParams.host,
              "-U", pgParams.user,
              "-d", pgParams.db,
              "-w",
              "-1",
              "-f", "${Docker.CONTAINER_MOUNT_DIR}/sql/create-schema.sql")

            environment(Postgres.ENV_PASSWORD, pgParams.password)
        }
    }
}

open class PgAddData : AbstractTask() {
    @TaskAction
    fun run() {
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "exec", Docker.CONTAINER_NAME,
              "psql",
              "-h", pgParams.host,
              "-U", pgParams.user,
              "-d", pgParams.db,
              "-w",
              "-1",
              "-f", "${Docker.CONTAINER_MOUNT_DIR}/sql/insert.sql")

            environment(Postgres.ENV_PASSWORD, pgParams.password)
        }
    }
}

/**
 * From here on out, these tasks will interact with any database pointed by JDBC_DATABASE_URL
 *  - Use of DataSource to avoid shell/OS incompatibility issues (multi-platform)
 *  - Force the creation of the DataSource for every single task (i.e. do not cache
 *  the database connection configs, which allows the user to change the JDBC_DATABASE_URL
 *  for applying the tasks to another database without the need for ./gradlew clean -p buildSrc)
 */
object Db {
  fun getConnection() = tryGetConnection()

  private fun genDataSource() = PGSimpleDataSource().apply {
    val url: String? = System.getenv("JDBC_DATABASE_URL")
    setUrl(url ?: throw Error(" ppip pi p"))
  }

  private fun tryGetConnection(): Connection {
    val ds = genDataSource()
    val start = System.currentTimeMillis()
    val timeout = 120000L
    val tick = 2000L
    while (System.currentTimeMillis() - start < timeout) {
      try {
        return ds.connection

      } catch (con: SQLException) {
        println("Database connection attempt failed. Retrying...")
        Thread.sleep(tick)
      }
    }
    throw IllegalStateException("Could not establish a connection to the database. Killing server.")
  }
}

open class PgInsertReadToken : AbstractTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:api:read")
    }
}

open class PgInsertWriteToken : AbstractTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:api:write")
    }
}

open class PgInsertIssueToken : AbstractTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:token:issue")
    }
}

private data class TokenDbParams(
  val hash: String,
  val isValid: Boolean,
  val issuedAt: Long,
  val expiredAt: Long,
  val scope: String,
  val clientId: Int,
  val base64Token: String)

class Token {
    fun create(scope: String) {
        val params = getTokenReferences(scope)
        val cid = 500
        val json = PGobject(); json.type = "jsonb"; json.value = """{"scope":"$scope", "client_id": ${cid}}"""
        val con = Db.getConnection()
        val st = con.prepareStatement("INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,claims) VALUES (?,?,?,?,?);")
        st.setString(1, params.hash)
        st.setBoolean(2, params.isValid)
        st.setLong(3, params.issuedAt)
        st.setLong(4, params.expiredAt)
        st.setObject(5, json)
        print("Your token reference is ${params.base64Token}")
    }

    private fun getTokenReferences(scope: String): TokenDbParams {
        val tokenGenerator = TokenGenerator()
        val randomString = tokenGenerator.generateRandomString()
        val base64Token = tokenGenerator.encodeBase64url(randomString)
        val tokenHash = tokenGenerator.getHash(randomString)
        val currTime = System.currentTimeMillis()
        val expirationTime = currTime + 1000*60*60

        return TokenDbParams(
          tokenHash,
          true,
          currTime,
          expirationTime,
          scope,
          500,
          base64Token
        )
    }
}

class DevNull : OutputStream() {
    override fun write(p0: Int) {
        // sink
    }
}

