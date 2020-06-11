package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.TokenGenerator.Companion.buildToken
import org.ionproject.core.accessControl.TokenGenerator.Companion.encodeBase64
import org.ionproject.core.accessControl.TokenGenerator.Companion.generateRandomString
import org.ionproject.core.accessControl.TokenGenerator.Companion.getHash
import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class BootTokenGeneration(private val authRepo: AuthRepo) {
    val issueScope = "urn:org:ionproject:scopes:token:issue"

    @EventListener(ApplicationReadyEvent::class)
    fun startup() {
        generateTokens()
    }

    /**
     * The process of generating tokens must be
     * after the server is completely initialized to accept
     * requests.
     */
    fun generateTokens() {
        logger.info("Starting token generation...")

        //generate the token needed for issuing other tokens
        val issueToken = generateToken()
        logger.info("Issue token is $issueToken")
        logger.info("Token generation is done.")
    }

    /**
     * Generates once at boot time an issue token
     * for the API. Used to issue the write and read token
     */
    private fun generateToken(): String {
        val tokenRaw = generateRandomString()
        val tokenBase64 = encodeBase64(tokenRaw)
        val tokenHash = getHash(tokenRaw)

        val token =  buildToken(tokenHash, System.currentTimeMillis(), issueScope, 0)
        authRepo.storeToken(token)  //token needs to be stored so it can be used to issue other tokens

        return tokenBase64
    }

    //Should previous tokens be cleaned on system restart?
    private fun cleanupTokens() {
        logger.info("Cleaning previous tokens...")
        TODO()
    }
}
