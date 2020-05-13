
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskAction
import java.net.URI

object Postgres {

    data class PgDb(val host: String, val port: String, val db: String, val user: String, val password: String)

    val pgPrms: PgDb by lazy {

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
            pgMap["user"] ?: error("missing user on "), pgMap["password"] ?: error("missing password"))

        println("Using DB: $prms")

        prms
    }
}

open class PgStart : AbstractTask() {

    @TaskAction
    fun run() {
        val pgPrms = Postgres.pgPrms
        project.exec {
            commandLine("docker", "run",
                "--name", "pg-container",
                "-e", "POSTGRES_PASSWORD=${pgPrms.password}",
                "-p", "5432:5432/tcp",
                "-d", "postgres")
        }
    }
}

open class PgStop : AbstractTask() {
    @TaskAction
    fun run() {
        project.exec {
            commandLine("docker", "rm", "--force", "pg-container")
        }
    }
}

open class PgDropDb : AbstractTask() {
    @TaskAction
    fun run() {
        val pgPrms = Postgres.pgPrms
        project.exec {
            commandLine("dropdb", "--if-exists",
                "-h", pgPrms.host,
                "-U", pgPrms.user,
                "-w", pgPrms.db)

            environment("PGPASSWORD", pgPrms.password)
        }
    }
}

open class PgCreateDb : AbstractTask() {
    @TaskAction
    fun greet() {
        val pgPrms = Postgres.pgPrms
        project.exec {
            commandLine("createdb",
                "-h", pgPrms.host,
                "-U", pgPrms.user,
                "-w", pgPrms.db)

            environment("PGPASSWORD", pgPrms.password)
        }
    }
}

open class PgInitSchema : AbstractTask() {
    @TaskAction
    fun run() {
        val pgPrms = Postgres.pgPrms
        project.exec {
            commandLine("psql",
                "-h", pgPrms.host,
                "-U", pgPrms.user,
                "-d", pgPrms.db,
                "-w",
                "-1",
                "-f", "src/test/resources/sql/create-schema.sql")

            environment("PGPASSWORD", pgPrms.password)
        }
    }
}

open class PgAddData : AbstractTask() {
    @TaskAction
    fun run() {
        val pgPrms = Postgres.pgPrms
        project.exec {
            commandLine("psql",
                "-h", pgPrms.host,
                "-U", pgPrms.user,
                "-d", pgPrms.db,
                "-w",
                "-1",
                "-f", "src/test/resources/sql/insert.sql")

            environment("PGPASSWORD", pgPrms.password)
        }
    }
}
