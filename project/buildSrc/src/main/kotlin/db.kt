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
    const val DEFAULT_PORT = 5432
    const val CONNECTION_STRING_ENV_NAME = "JDBC_DATABASE_URL"
    private const val DEFAULT_CONNECTION_STRING =
        "jdbc:postgresql://localhost:5432/ion?user=postgres&password=changeit"
    private const val CONNECTION_STRING_FORMAT =
        "jdbc:postgresql://<host>:<port>/<db>?user=<username>&password=<user password>"

    data class PgDb(
        val host: String,
        val port: Int,
        val db: String,
        val user: String,
        val password: String
    )

    fun getPgParams(): PgDb {
        val pgUrl: String = System.getenv(CONNECTION_STRING_ENV_NAME).let {
            if (it == null || it.isEmpty()) {
                println("no $CONNECTION_STRING_ENV_NAME environment variable found, using default value")
                DEFAULT_CONNECTION_STRING
            } else {
                it
            }
        }

        val pgUri = URI(pgUrl.substring(5))
        val pgMap = pgUri.query.split('&')
            .map { it.split('=') }
            .map { it[0] to it[1] }
            .toMap()

        val prms = PgDb(
            pgUri.host, pgUri.port.toInt(), pgUri.path.substring(1),
            pgMap["user"] ?: error("Missing the user. Connection string format: $CONNECTION_STRING_FORMAT"),
            pgMap["password"] ?: error("Missing the password. Connection string format: $CONNECTION_STRING_FORMAT")
        )
        println("Using DB: $prms")

        return prms
    }
}

object Docker {
    const val CONTAINER_NAME = "pg-container"
    const val CONTAINER_PORT = 5432
    const val RESOURCES_FOLDER = "src/test/resources"

    fun isContainerRunning(project: Project): Boolean {
        val pipe = ByteArrayOutputStream()
        project.exec {
            commandLine(
                "docker", "ps",
                "-a",
                "-q",
                "-f", "name=$CONTAINER_NAME"
            )

            standardOutput = pipe
        }

        // the pipe will be empty if the container does not exist
        return pipe.size() > 0
    }

    fun tryConnectDb(project: Project, pgParams: Postgres.PgDb): Boolean = try {
        project.exec {
            commandLine(
                "docker", "exec", CONTAINER_NAME,
                "psql",
                "-h", "localhost",
                "-U", pgParams.user,
                "-d", pgParams.db,
                "-p", Postgres.DEFAULT_PORT,
                "-w",
                "-1",
                "-c", "select"
            )

            // sink
            errorOutput = DevNull()
            standardOutput = DevNull()
        }
        true
    } catch (e: ExecException) {
        false
    }

    fun createDbContainer(project: Project, pgParams: Postgres.PgDb) {
        project.exec {
            commandLine(
                "docker", "build",
                "-t", CONTAINER_NAME,
                "-f", "${project.rootDir.absolutePath}/$RESOURCES_FOLDER/dockerfile_db",
                "${project.rootDir.absolutePath}/$RESOURCES_FOLDER"
            )
        }

        project.exec {
            commandLine(
                "docker", "run",
                "--name", CONTAINER_NAME,
                "-e", "POSTGRES_PASSWORD=${pgParams.password}",
                "-e", "POSTGRES_USER=${pgParams.user}",
                "-e", "POSTGRES_DB=${pgParams.db}",
                "-p", "${pgParams.port}:$CONTAINER_PORT/tcp",
                "-d", CONTAINER_NAME
            )
        }
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

        val pgParams = Postgres.getPgParams()
        println("Creating DB image and container...")
        Docker.createDbContainer(project, pgParams)
        println("Container created and daemonized.")

        val tick = 250L
        while (!Docker.tryConnectDb(project, pgParams)) {
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

        val pgParams = Postgres.getPgParams()
        println("Container \"${Docker.CONTAINER_NAME}\" not running. Creating...")
        Docker.createDbContainer(project, pgParams)

        val tick = 250L
        while (!Docker.tryConnectDb(project, pgParams)) {
            println("Polling for DBMS availability...")
            Thread.sleep(tick)
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
    fun useConnection(e: (Connection) -> Unit) {
        tryGetConnection().use { e(it) }
    }

    private fun genDataSource() = PGSimpleDataSource().apply {
        val url: String? = System.getenv(Postgres.CONNECTION_STRING_ENV_NAME)
        setUrl(url ?: throw Error("${Postgres.CONNECTION_STRING_ENV_NAME} connection string not set"))
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

open class PgInsertRevokeToken : AbstractTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:api:revoke")
    }
}

private data class TokenDbParams(
    val hash: String,
    val isValid: Boolean,
    val issuedAt: Long,
    val expiredAt: Long,
    val scope: String,
    val clientId: Int,
    val base64Token: String
)

class Token {
    fun create(scope: String) {
        val params = getTokenReferences(scope)
        val json = PGobject(); json.type = "jsonb"; json.value = """{"scope":"$scope"}"""
        Db.useConnection {
            val st =
                it.prepareStatement("INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,claims) VALUES (?,?,?,?,?);")
            st.setString(1, params.hash)
            st.setBoolean(2, params.isValid)
            st.setLong(3, params.issuedAt)
            st.setLong(4, params.expiredAt)
            st.setObject(5, json)
            st.execute()
        }
        print("Your token reference is ${params.base64Token}")
    }

    private fun getTokenReferences(scope: String): TokenDbParams {
        val tokenGenerator = TokenGenerator()
        val randomString = tokenGenerator.generateRandomString()
        val base64Token = tokenGenerator.encodeBase64url(randomString)
        val tokenHash = tokenGenerator.getHash(randomString)
        val currTime = System.currentTimeMillis()
        val expirationTime = currTime + 1000L * 60 * 60 * 365

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

