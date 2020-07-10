package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.sql.AuthRepo
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.calendar.CalendarRepo
import org.ionproject.core.common.customExceptions.BadRequestException
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class AccessServices(
    private val authRepo: AuthRepo,
    private val tokenGenerator: TokenGenerator,
    private val calRepo: CalendarRepo
) {

    fun generateToken(scope: String): TokenRepr {
        if (!authRepo.checkScopeExistence(scope))
            throw BadRequestException("Inexistent scope")

        //generate the raw string
        val tokenString = tokenGenerator.generateRandomString()

        //base64url encode the string
        val tokenBase64encoded = tokenGenerator.encodeBase64url(tokenString)

        //hash the raw string & store it
        val tokenHash = tokenGenerator.getHash(tokenString)
        val issueTime = System.currentTimeMillis()
        val token = tokenGenerator.buildToken(tokenHash, issueTime, scope)

        authRepo.storeToken(token)
        return TokenRepr(tokenBase64encoded, issueTime)
    }

    /**
     * Revokes the presented token reference in the body
     */
    fun revokeToken(tokenHash: String) {
        authRepo.revokeToken(tokenHash)
    }

    /**
     * Revokes the derived tokens of the presented token reference in the body
     * e.g. revoke all import url's originated by the read token presented
     */
    fun revokeChild(tokenHash: String) {
        authRepo.revokeChild(tokenHash)
    }

    /**
     * Revokes the derived tokens and the presented token
     */
    fun revokePresentedAndChild(tokenHash: String) {
        authRepo.revokePresentedAndChild(tokenHash)
    }

    /**
     * Checks if class calendar exists, if it does generate or share token
     */
    fun generateImportClassCalendar(
        cid: Int,
        calterm: String,
        query: MultiValueMap<String, String>,
        tokenHash: String
    ): String {
        val calendar = calRepo.getClassCalendar(cid, calterm, query)

        if (calendar != null)
            return generateImportToken(tokenHash)
        else
            throw BadRequestException("The class calendar you tried to import doesn't exist.")
    }

    /**
     * Checks if class section calendar exists, if it does generate or share token
     */
    fun generateImportClassSectionCalendar(
        sid: String,
        calterm: String,
        cid: Int,
        query: MultiValueMap<String, String>,
        tokenHash: String
    ): String {

        val calendar = calRepo.getClassSectionCalendar(cid, calterm, sid, query)

        if (calendar != null)
            return generateImportToken(tokenHash)
        else
            throw BadRequestException("The class section calendar you tried to import doesn't exist.")
    }

    /**
     * Generates an import link token, this only happens if the calendar exists
     * to avoid denial of service generating millions of import url's to non existent calendars...
     */
    fun generateImportToken(fatherTokenHash: String): String {
        val tokenReference = authRepo.generateImportToken(fatherTokenHash)
        return "access_token=$tokenReference"
    }

}
