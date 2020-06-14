package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.common.customExceptions.BadRequestException
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

@Component
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

    fun decodeBase64url(tokenBase64: String): String {
        val decoder : ByteArray
        try {
            decoder = Base64
                    .getUrlDecoder()
                    .decode(tokenBase64)
            return String(decoder)
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("The token value is incorrect")
        }
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

    /**
     * Given the generated information build a token object
     *
     * client_id 0 in the beta phase of the access manager holds no special value
     */
    fun buildToken(tokenHash: String, issueTime: Long, scope: String, clientId: Int) : TokenEntity {
        val claims = ClaimsEntity(clientId, scope)
        return TokenEntity(tokenHash, true, issueTime, issueTime + TOKEN_DURATION, claims)
    }
}