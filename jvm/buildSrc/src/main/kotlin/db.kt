import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PGobject
import java.sql.Connection
import java.sql.SQLException


const val CONNECTION_STRING_ENV_NAME = "JDBC_DATABASE_URL"

object Db {
    fun useConnection(e: (Connection) -> Unit) {
        tryGetConnection().use { e(it) }
    }

    private fun genDataSource() = PGSimpleDataSource().apply {
        val url: String? = System.getenv(CONNECTION_STRING_ENV_NAME)
        println(url)
        setUrl(url ?: throw Error("$CONNECTION_STRING_ENV_NAME connection string not set"))
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

open class PgInsertReadToken : DefaultTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:api:read")
    }
}

open class PgInsertIssueToken : DefaultTask() {
    @TaskAction
    fun run() {
        Token().create("urn:org:ionproject:scopes:token:issue")
    }
}

open class PgInsertRevokeToken : DefaultTask() {
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
