package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.TokenGenerator.Companion.buildToken
import org.ionproject.core.accessControl.TokenGenerator.Companion.decodeBase64
import org.ionproject.core.accessControl.TokenGenerator.Companion.encodeBase64
import org.ionproject.core.accessControl.TokenGenerator.Companion.generateRandomString
import org.ionproject.core.accessControl.TokenGenerator.Companion.getHash
import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.ionproject.core.accessControl.representations.TokenRepr
import org.springframework.stereotype.Component

@Component
class AccessServices(private val authRepo: AuthRepo) {

    fun generateToken(scope: String) : TokenRepr {
        //generate the raw string
        val tokenString = generateRandomString()

        //base64url encode the string
        val tokenBase64encoded = encodeBase64(tokenString)

        //hash the raw string & store it
        val tokenHash = getHash(tokenString)
        val issueTime = System.currentTimeMillis()
        val token = buildToken(tokenHash, issueTime, scope, 0)

        authRepo.storeToken(token)
        return TokenRepr(tokenBase64encoded, issueTime)
    }

    /**
     * A possible optimization is to insert the hash in the request
     * at the moment its first checked during the PDP
     */
    fun revokeToken(token: String) {
        val base64reference = token.split(" ")[1]
        val hash = getHash(decodeBase64(base64reference))
        authRepo.revokeToken(hash)
    }
}