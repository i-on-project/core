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
internal class TestBootTokenGeneration(
    private val authRepo: AuthRepo,
    private val tokenGenerator: TokenGenerator
) {
    val readScope = "urn:org:ionproject:scopes:api:read"
    val issueScope = "urn:org:ionproject:scopes:token:issue"
    val revokeScope = "urn:org:ionproject:scopes:api:revoke"

    @EventListener(ApplicationReadyEvent::class)
    fun startup() {
        // check the database for tokens before issuing
        generateTokens()
    }

    fun generateTokens() {
        // generate the token needed for issuing other tokens
        val token = generateToken(readScope)
        val issueToken = generateToken(issueScope)
        val revokeToken = generateToken(revokeScope)

        // with the issueToken issue read & write token
        issueTokenTest = "Bearer $issueToken"
        readTokenTest = "Bearer $token"
        revokeTokenTest = "Bearer $revokeToken"
    }

    /**
     * Issues a token and stores it
     */
    private fun generateToken(scope: String): String {
        val tokenRaw = tokenGenerator.generateRandomString()
        val tokenBase64 = tokenGenerator.encodeBase64url(tokenRaw)
        val tokenHash = tokenGenerator.getHash(tokenRaw)

        val token = tokenGenerator.buildToken(tokenHash, System.currentTimeMillis(), scope)
        authRepo.storeToken(token) // token needs to be stored so it can be used to issue other tokens

        return tokenBase64
    }
}
