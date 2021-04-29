import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PGobject
import java.io.OutputStream
import java.net.URI
import java.sql.Connection
import java.sql.SQLException
import java.time.Duration
import javax.sql.DataSource

data class PgDb(
    val host: String,
    val port: Int,
    val db: String,
    val user: String,
    val password: String,
    val url: String
)

const val CONNECTION_STRING_ENV_NAME = "JDBC_DATABASE_URL"
const val RESOURCES_FOLDER = "src/test/resources"

object Postgres {
    private const val DEFAULT_CONNECTION_STRING =
        "jdbc:postgresql://localhost:5432/ion?user=postgres&password=changeit"
    private const val CONNECTION_STRING_FORMAT =
        "jdbc:postgresql://<host>:<port>/<db>?user=<username>&password=<user password>"

    private val postgresConnectionTimeout = Duration.ofMinutes(2)
        .toSeconds()
        .toInt()

    val pgParams: PgDb by lazy {
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

        val params = PgDb(
            pgUri.host, pgUri.port, pgUri.path.substring(1),
            pgMap["user"] ?: error("Missing the user. Connection string format: $CONNECTION_STRING_FORMAT"),
            pgMap["password"] ?: error("Missing the password. Connection string format: $CONNECTION_STRING_FORMAT"),
            pgUrl
        )

        println("Using DB: $params")
        params
    }

    private val dataSource: DataSource
        get() = PGSimpleDataSource().apply {
            setURL(pgParams.url)
            connectTimeout = postgresConnectionTimeout
        }

    fun tryConnectDb(): Connection = try {
        dataSource.connection
    } catch (e: SQLException) {
        throw e
    }
}

object Db {
    fun useConnection(e: (Connection) -> Unit) {
        Postgres.tryConnectDb().use { e(it) }
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

