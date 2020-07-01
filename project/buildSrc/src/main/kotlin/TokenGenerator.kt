import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class TokenGenerator {
    private val STRING_BIT_SIZE = 256
    private val STRING_LENGTH = STRING_BIT_SIZE / 8

    private val HASH_ALGORITHM = "SHA-256"
    private val TOKEN_DURATION: Long = 1000 * 60 * 60 * 24 * 20 //Time in milliseconds before token expiring

    fun generateRandomString(): ByteArray {
        val bytes = ByteArray(STRING_LENGTH)
        SecureRandom().nextBytes(bytes)

        return bytes
    }

    fun encodeBase64url(tokenBytes: ByteArray): String {
        val token = Base64
            .getUrlEncoder()                //replaces '+' , '/'  for '-' , '_'
            .withoutPadding()               //Remove the '='
            .encodeToString(tokenBytes)

        println("string token:$token")
        return token
    }

    /**
     * Gets the hash of the string passed
     */
    fun getHash(tokenBytes: ByteArray): String {
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        val digest = md.digest(tokenBytes)

        //Print bytes in hexadecimal format with padding in case of insufficient chars (used to index the token table)
        val hash = digest.fold("", { str, it -> str + "%02x".format(it) })

        println("hash:$hash")
        return hash
    }

}
