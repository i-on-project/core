package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.representations.JWTHeaderRepr
import org.ionproject.core.accessControl.representations.JWTPayloadRepr
import org.ionproject.core.accessControl.representations.serializeComponent
import org.ionproject.core.common.customExceptions.BadRequestException
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class TokenGenerator {
    private val STRING_BIT_SIZE = 256
    private val STRING_LENGTH = STRING_BIT_SIZE / 8

    private val HASH_ALGORITHM = "SHA-256"
    private val TOKEN_DURATION: Long = 1000 * 60 * 60 * 24 * 20 //Time in milliseconds before token expiring

    private val JWT_ALGORITHM = "HmacSHA256"
    private val SECRET_KEY = System.getenv("secretKey")

    fun generateRandomString(): ByteArray {
        val bytes = ByteArray(STRING_LENGTH)
        SecureRandom().nextBytes(bytes)

        return bytes
    }

    fun encodeBase64url(tokenBytes: ByteArray): String {
        return Base64
            .getUrlEncoder()                //replaces '+' , '/'  for '-' , '_'
            .withoutPadding()               //Remove the '='
            .encodeToString(tokenBytes)
    }

    fun decodeBase64url(tokenBase64: String): ByteArray {
        val decoder: ByteArray
        try {
            decoder = Base64
                .getUrlDecoder()
                .decode(tokenBase64)

            return decoder
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("The token value is incorrect")
        }
    }

    /**
     * Gets the hash of the string passed
     */
    fun getHash(tokenBytes: ByteArray): String {
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        val digest = md.digest(tokenBytes)

        //Print bytes in hexadecimal format with padding in case of insufficient chars (used to index the token table)

        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    /**
     * Given the generated information build a token object
     *
     * client_id 0 in the beta phase of the access manager holds no special value
     */
    fun buildToken(tokenHash: String, issueTime: Long, scope: String, clientId: Int): TokenEntity {
        val claims = ClaimsEntity(clientId, scope)
        return TokenEntity(tokenHash, true, issueTime, issueTime + TOKEN_DURATION, claims)
    }

    /**
     * Generates a JWT token without signature: header.payload
     */
    fun generateImportToken(url: String, clientId: Int) : String {
        val header = JWTHeaderRepr("JWT", "HS256")

        val iat = System.currentTimeMillis()
        val exp = iat + TOKEN_DURATION
        val payload = JWTPayloadRepr(clientId, url, iat, exp)

        val headerBytes = serializeComponent(header).toByteArray()
        val payloadBytes = serializeComponent(payload).toByteArray()

        return encodeBase64url(headerBytes) + "." + encodeBase64url(payloadBytes)
    }

    /**
     * Signs a JWT
     *  HMACSHA256(
     *      base64UrlEncode(header) + "." +
     *      base64UrlEncode(payload),
     *      256-bit-secret
     *  )
     *
     *  The first two base64UrlEncoded parts are already present in the jwt
     */
    fun signJWT(jwt: String) : String {
        val alg = Mac.getInstance(JWT_ALGORITHM)
        val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), JWT_ALGORITHM)

        alg.init(secretKey)

        val signatureBytes = alg.doFinal(jwt.toByteArray())
        return signatureBytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    /**
     * Verifies if the signature on the received jwt is valid,
     * this operations rebuilds the signature and compares the expected with the actual value.
     */
    fun verifySignatureJWT(jwt: String) : Boolean {
        val parts = jwt.split(".")

        val requestJwtSignature = parts[2]
        val headerPayload = parts[0] + "." + parts[1]
        val generatedSignature = signJWT(headerPayload)

        return requestJwtSignature == generatedSignature
    }
}
