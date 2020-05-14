import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.net.URI

object Postgres {
    const val SUPER_USER = "postgres"
    const val ENV_PASSWORD = "PGPASSWORD"

    data class PgDb(
      val host: String,
      val port: String,
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

        val prms = PgDb(pgUri.host, pgUri.port.toString(), pgUri.path.substring(1),
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
    const val HOST_PORT = 5432
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
              "-p", "${Docker.HOST_PORT}:${Docker.CONTAINER_PORT}/tcp",
              "-v", "${project.rootDir.absolutePath}/${Docker.HOST_MOUNT_DIR}:${Docker.CONTAINER_MOUNT_DIR}",
              "-d", Docker.IMAGE_NAME)
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
            }
            return
        }
        println("Container \"${Docker.CONTAINER_NAME}\" not running. Creating...")
        val pgParams = Postgres.pgParams
        project.exec {
            commandLine("docker", "run",
              "--name", Docker.CONTAINER_NAME,
              "-e", "POSTGRES_PASSWORD=${pgParams.password}",
              "-p", "${Docker.HOST_PORT}:${Docker.CONTAINER_PORT}/tcp",
              "-v", "${project.rootDir.absolutePath}/${Docker.HOST_MOUNT_DIR}" +
              ":${Docker.CONTAINER_MOUNT_DIR}",
              "-d", Docker.IMAGE_NAME)
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

