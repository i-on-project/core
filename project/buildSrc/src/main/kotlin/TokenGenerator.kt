import java.security.MessageDigest
import java.util.*

class TokenGenerator {
    companion object {
        private const val STRING_SIZE = 256
        private const val STRING_LENGTH = STRING_SIZE / 8

        private const val HASH_ALGORITHM = "SHA-256"
        private const val TOKEN_DURATION : Long = 1000 * 60 * 60 * 24 * 20 //Time in milliseconds before token expiring

        private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') //List of chars used in the random string generation

        fun generateRandomString() : String {
            val randomString = (1..STRING_LENGTH)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("");

            return randomString
        }

        fun encodeBase64(rawValue : String) : String {
            val rawValueBytes = rawValue.toByteArray()
            val encoder = Base64.getEncoder().encode(rawValueBytes)
            return String(encoder)
        }

        fun decodeBase64(tokenBase64: String): String {
            val decoder = Base64.getDecoder().decode(tokenBase64)
            return String(decoder)
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
}