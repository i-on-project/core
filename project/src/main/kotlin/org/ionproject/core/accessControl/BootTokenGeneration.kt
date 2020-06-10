package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.TokenGenerator.Companion.buildToken
import org.ionproject.core.accessControl.TokenGenerator.Companion.encodeBase64
import org.ionproject.core.accessControl.TokenGenerator.Companion.generateRandomString
import org.ionproject.core.accessControl.TokenGenerator.Companion.getHash
import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
import org.ionproject.core.accessControl.representations.TokenIssueDetails
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Uri
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

var readToken : String = ""
var writeToken : String = ""

@Component
class Boot {
    @EventListener(ApplicationReadyEvent::class)
    fun startup() {
        val bootGen = BootTokenGeneration(AuthRepoImpl(TransactionManagerImpl(DataSourceHolder)))
        bootGen.generateTokens()
    }
}


@Component
class BootTokenGeneration(private val authRepo: AuthRepo) {
    val readScope = "urn:org:ionproject:scopes:api:read"
    val writeScope = "urn:org:ionproject:scopes:api:write"
    val issueScope = "urn:org:ionproject:scopes:token:issue"

    val uri = "http://localhost:8080"

    /**
     * The process of generating tokens must be
     * after the server is completely initialized to accept
     * requests.
     */

    fun generateTokens() {
        logger.info("Starting token generation...")

        //generate the token needed for issuing other tokens
        val issueToken = generateIssueToken()

        //with the issueToken issue read & write token
        readToken = issueToken(issueToken, readScope)
        writeToken = issueToken(issueToken, writeScope)


        logger.info("Read token is: $readToken")
        logger.info("Write token is: $writeToken")
        logger.info("Token generation is done.")
    }

    /**
     * Generates once at boot time an issue token
     * for the API. Used to issue the write and read token
     */
    private fun generateIssueToken(): String {
        val tokenRaw = generateRandomString()
        val tokenBase64 = encodeBase64(tokenRaw)
        val tokenHash = getHash(tokenRaw)

        val token =  buildToken(tokenHash, System.currentTimeMillis(), issueScope)
        authRepo.storeToken(token)  //token needs to be stored so it can be used to issue other tokens

        return tokenBase64
    }

    /**
     * Makes a request to the "/issueToken" endpoint to issue
     * a token and returns the token received.
     *
     * Sends as body a TokenIssueDetails
     * Receives as response a TokenRepr
     */
    private fun issueToken(issueToken: String, scope: String): String {
        val requestBody = serialize(TokenIssueDetails(scope))
        val response = doPut(issueToken, requestBody)

        return response.token
    }

    private fun doPut(issueToken: String, requestBody: String): TokenRepr {
        val url = URL(uri+Uri.issueToken)
        val response = StringBuffer()

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "PUT"
            doOutput = true

            setRequestProperty("Authorization", "Bearer $issueToken")
            setRequestProperty("Content-Type", "application/json")

            val wr = OutputStreamWriter(outputStream)
            wr.write(requestBody)
            wr.close()
            print("Sending token issue request...")

            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()

                while(inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
            }
        }

        return deserialize(response.toString())
    }

    //convert object to json string (stringify)
    private fun serialize(requestBody: TokenIssueDetails): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(requestBody)
    }

    //convert json string to object (parse)
    private fun deserialize(claims: String): TokenRepr {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(claims, TokenRepr::class.java)
    }


    /**
     * Everytime the system is restarted this class will run
     * generating new tokens, to make it worse with the tests will happen the same.
     *
     * The database will end up being filled with useless tokens.
     * Therefore there will be a cleanup on the token table everytime the system is restarted.
     *
     * In case this behavior is not desired its easy to remove.
     */
    private fun cleanupTokens() {
        logger.info("Cleaning previous tokens...")
    }
}
