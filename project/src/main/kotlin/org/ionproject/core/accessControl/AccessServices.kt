package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.TokenGenerator.Companion.buildToken
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
        val token = buildToken(tokenHash, issueTime, scope)

        authRepo.storeToken(token)
        return TokenRepr(tokenBase64encoded, issueTime)
    }


    fun revokeToken(token: String) {
        TODO()
    }
}