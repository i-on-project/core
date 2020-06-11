package org.ionproject.core.utils


import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * It creates at startup the tokens needed to pass the tests,
 * if tokens were hardcoded they would get leaked on the repository
 * code upload.
 *
 * It currently lacks a way to clear the tokens after the tests.
 *
 * client_id -1 represents tokens created in test mode
 */
@Component
internal class TestBootTokenGeneration(private val authRepo: AuthRepo) {
    val readScope = "urn:org:ionproject:scopes:api:read"
    val writeScope = "urn:org:ionproject:scopes:api:write"
    val issueScope = "urn:org:ionproject:scopes:token:issue"

    @EventListener(ApplicationReadyEvent::class)
    fun startup() {
        generateTokens()
    }

    fun generateTokens() {
        //generate the token needed for issuing other tokens
        val token = generateToken(readScope, -1)
        val issueToken = generateToken(issueScope, -1)

        //with the issueToken issue read & write token
        issueTokenTest = "Bearer $issueToken"
        readTokenTest = "Bearer $token"
        //val writeToken = issueToken(issueToken, writeScope)
    }

    /**
     * Issues a token and stores it
     */
    private fun generateToken(scope: String, clientId: Int): String {
        val tokenRaw = TokenGenerator.generateRandomString()
        val tokenBase64 = TokenGenerator.encodeBase64(tokenRaw)
        val tokenHash = TokenGenerator.getHash(tokenRaw)

        val token = TokenGenerator.buildToken(tokenHash, System.currentTimeMillis(), scope, clientId)
        authRepo.storeToken(token)  //token needs to be stored so it can be used to issue other tokens

        return tokenBase64
    }

}

