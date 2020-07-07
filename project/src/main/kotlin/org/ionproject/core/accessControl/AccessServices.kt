package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.pap.entities.ClaimsEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.ionproject.core.accessControl.representations.JWT
import org.ionproject.core.accessControl.representations.JWTHeaderRepr
import org.ionproject.core.accessControl.representations.JWTPayloadRepr
import org.ionproject.core.accessControl.representations.TokenRepr
import org.springframework.stereotype.Component

@Component
class AccessServices(private val authRepo: AuthRepo, private val tokenGenerator: TokenGenerator) {

    fun generateToken(scope: String): TokenRepr {
        //generate the raw string
        val tokenString = tokenGenerator.generateRandomString()

        //base64url encode the string
        val tokenBase64encoded = tokenGenerator.encodeBase64url(tokenString)

        //hash the raw string & store it
        val tokenHash = tokenGenerator.getHash(tokenString)
        val issueTime = System.currentTimeMillis()
        val token = tokenGenerator.buildToken(tokenHash, issueTime, scope, 0)

        authRepo.storeToken(token)
        return TokenRepr(tokenBase64encoded, issueTime)
    }

    /**
     * A possible optimization is to insert the hash in the request
     * at the moment its first checked during the PDP
     */
    fun revokeToken(token: String) {
        val hash = tokenGenerator.getHash(tokenGenerator.decodeBase64url(token))
        authRepo.revokeToken(hash)
    }

    /**
     * Generates a JWT according to RFC 7519
     */
    fun generateImportToken(url: String, clientId: Int) : String {
        val jwtWithoutSignature = tokenGenerator.generateImportToken(url, clientId)
        val signature = tokenGenerator.signJWT(jwtWithoutSignature)
        val jwtSigned = "$jwtWithoutSignature.$signature"

        return "access_token=$jwtSigned"
    }

}
