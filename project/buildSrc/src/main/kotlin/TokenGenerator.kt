
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class TokenGenerator {
    private val STRING_BIT_SIZE = 256
    private val STRING_LENGTH = STRING_BIT_SIZE / 8

    private val HASH_ALGORITHM = "SHA-256"
    private val TOKEN_DURATION : Long = 1000 * 60 * 60 * 24 * 20 //Time in milliseconds before token expiring

    fun generateRandomString() : String {
        val bytes = ByteArray(STRING_LENGTH)
        SecureRandom().nextBytes(bytes)

        return String(bytes)
    }

    fun encodeBase64url(rawValue : String) : String {
        return Base64
                .getUrlEncoder()                //replaces '+' , '/'  for '-' , '_'
                .withoutPadding()               //Remove the '='
                .encodeToString(rawValue.toByteArray(StandardCharsets.UTF_8))
    }

    /**
     * Gets the hash of the string passed
     */
    fun getHash(tokenRaw: String): String {
        val bytes = tokenRaw.toByteArray()
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        val digest = md.digest(bytes)

        //Print bytes in hexadecimal format with padding in case of insufficient chars (used to index the token table)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}
