package org.ionproject.core.userApi.auth

import com.ninjasquad.springmockk.SpykBean
import io.mockk.Matcher
import io.mockk.coVerify
import org.ionproject.core.common.Uri
import org.ionproject.core.common.email.EmailService
import org.ionproject.core.common.email.EmailType
import org.ionproject.core.userApi.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.userApi.auth.model.AuthRequestInput
import org.ionproject.core.userApi.auth.model.AuthSuccessfulResponse
import org.ionproject.core.userApi.auth.model.AuthTokenInput
import org.ionproject.core.userApi.auth.model.AuthVerification
import org.ionproject.core.userApi.user.model.UserRevokeTokenInput
import org.ionproject.core.utils.ANDROID_CLIENT_ID
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.TEST_USER_AGENT
import org.ionproject.core.utils.WEB_CLIENT_ID
import org.ionproject.core.utils.WEB_CLIENT_NAME
import org.ionproject.core.utils.WEB_CLIENT_SECRET
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import java.util.concurrent.atomic.AtomicInteger

internal class UserAuthTest : ControllerTester() {

    private class EmailContentMatcher(
        authRequestId: String,
        val clientName: String,
        val userAgent: String
    ) : Matcher<String> {

        private val verifyUri = Uri.forAuthVerifyFrontend(authRequestId, "_secret_")
            .toString()
            .replace("_secret_", "[\\w-]+")

        private val htmlElement = "<a href=\"$verifyUri\">$verifyUri</a>"
            .replace("?", "\\?")

        private val regex = Regex(htmlElement)

        override fun match(arg: String?): Boolean {
            if (arg == null)
                return false

            return arg.contains(clientName) &&
                arg.contains(userAgent) &&
                arg.contains(regex)
        }
    }

    companion object {
        private const val SAMPLE_AUTH_REQUEST_ID = "400d1ef0-3e20-46de-9452-8c21a5039ba5"
        private const val SAMPLE_AUTH_REQUEST_SECRET = "6TdbtIAHwxA4_yEQQDhBd7tZHYZBIDJfbjJdzfYF69ZHiesUg_Y_UfMlsxOSXHGQMNjWRI9IxiSfUTENCp_Jvw"
        private const val CIBA_GRANT_TYPE = "urn:openid:params:grant-type:ciba"
        private const val REFRESH_GRANT_TYPE = "refresh_token"
        private const val REFRESH_TOKEN = "5XmRA2RJyVqEwVsYvRZjSvAZW-dykx-isixvBtwSjGUL87UpFunIIuZ1i9L6ywQcRmQF9u8ckgOGgyCXcL9wBg"
        private const val EMAIL_DOMAIN = "alunos.isel.pt"
        private const val EMAIL_SUBJECT = "ion - Verify your login attempt"

        private val emailCounter = AtomicInteger()

        fun getEmailAddress() = "dummy${emailCounter.getAndIncrement()}@$EMAIL_DOMAIN"
    }

    @SpykBean
    private lateinit var emailService: EmailService

    @Test
    fun `Initiate auth request and check email`() {
        val email = getEmailAddress()
        val authRequestInput = AuthRequestInput(
            "openid profile classes",
            "email",
            WEB_CLIENT_ID,
            WEB_CLIENT_SECRET,
            email
        )

        val result = doPost(Uri.forAuthBackChannel()) {
            content = jacksonMapper.writeValueAsString(authRequestInput)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val output = jacksonMapper.readValue(result.response.contentAsString, AuthRequestAcknowledgement::class.java)
        coVerify(exactly = 1) {
            emailService.sendEmail(
                email,
                EmailType.HTML,
                EMAIL_SUBJECT,
                match(EmailContentMatcher(output.authRequestId, WEB_CLIENT_NAME, TEST_USER_AGENT))
            )
        }
    }

    private fun doTokenRequest(tokenInput: AuthTokenInput, dsl: MockMvcResultMatchersDsl.() -> Unit) =
        doPost(Uri.forAuthToken()) {
            content = jacksonMapper.writeValueAsString(tokenInput)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            dsl()
        }.andReturn()

    private fun doTokenRevoke(tokenRevokeInput: UserRevokeTokenInput, dsl: MockMvcResultMatchersDsl.() -> Unit) =
        doDelete(Uri.forTokenRevoke()) {
            content = jacksonMapper.writeValueAsString(tokenRevokeInput)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            dsl()
        }.andReturn()

    @Test
    fun `Verify auth request and poll`() {
        val authVerification = AuthVerification(
            SAMPLE_AUTH_REQUEST_ID,
            SAMPLE_AUTH_REQUEST_SECRET
        )

        // poll the auth request
        val tokenPoll = AuthTokenInput(CIBA_GRANT_TYPE, WEB_CLIENT_ID, WEB_CLIENT_SECRET, SAMPLE_AUTH_REQUEST_ID, null)
        doTokenRequest(tokenPoll) {
            status { isBadRequest() }
            jsonPath("$.error") { value("authorization_pending") }
        }

        // verify the auth request
        doPost(Uri.forAuthVerify()) {
            content = jacksonMapper.writeValueAsString(authVerification)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()

        // poll with invalid client
        val tokenInvalidPoll = AuthTokenInput(CIBA_GRANT_TYPE, ANDROID_CLIENT_ID, null, SAMPLE_AUTH_REQUEST_ID, null)
        doTokenRequest(tokenInvalidPoll) {
            status { isBadRequest() }
            jsonPath("$.error") { value("unauthorized_client") }
        }

        // poll without client_secret
        val tokenInvalidPollSecret = AuthTokenInput(CIBA_GRANT_TYPE, WEB_CLIENT_ID, null, SAMPLE_AUTH_REQUEST_ID, null)
        doTokenRequest(tokenInvalidPollSecret) {
            status { isBadRequest() }
            jsonPath("$.error") { value("invalid_client") }
        }

        // poll the auth request
        doTokenRequest(tokenPoll) {
            status { is2xxSuccessful() }
            jsonPath("$.access_token") { exists() }
            jsonPath("$.refresh_token") { exists() }
            jsonPath("$.token_type") { value("Bearer") }
            jsonPath("$.id_token") { exists() }
        }

        doTokenRequest(tokenPoll) {
            status { isBadRequest() }
            jsonPath("$.error") { value("invalid_grant") }
        }
    }

    @Test
    fun `Refresh and revoke token`() {
        // refresh with invalid client
        val invalidTokenRefresh = AuthTokenInput(REFRESH_GRANT_TYPE, ANDROID_CLIENT_ID, null, null, REFRESH_TOKEN)
        doTokenRequest(invalidTokenRefresh) {
            status { isBadRequest() }
            jsonPath("$.error") { value("unauthorized_client") }
        }

        // refresh with invalid secret
        val invalidTokenRefreshSecret = AuthTokenInput(REFRESH_GRANT_TYPE, WEB_CLIENT_ID, null, null, REFRESH_TOKEN)
        doTokenRequest(invalidTokenRefreshSecret) {
            status { isBadRequest() }
            jsonPath("$.error") { value("invalid_client") }
        }

        // refresh
        val tokenRefresh = AuthTokenInput(REFRESH_GRANT_TYPE, WEB_CLIENT_ID, WEB_CLIENT_SECRET, null, REFRESH_TOKEN)
        val refreshRes = doTokenRequest(tokenRefresh) {
            status { is2xxSuccessful() }
            jsonPath("$.access_token") { exists() }
            jsonPath("$.refresh_token") { exists() }
            jsonPath("$.token_type") { value("Bearer") }
            jsonPath("$.id_token") { exists() }
        }

        val authToken = jacksonMapper.readValue(refreshRes.response.contentAsString, AuthSuccessfulResponse::class.java)
        val accessToken = authToken.accessToken
        val refreshToken = authToken.refreshToken

        // refresh slow down
        val tokenRefreshSlowDown = AuthTokenInput(REFRESH_GRANT_TYPE, WEB_CLIENT_ID, WEB_CLIENT_SECRET, null, refreshToken)
        doTokenRequest(tokenRefreshSlowDown) {
            status { isBadRequest() }
            jsonPath("$.error") { value("slow_down") }
        }

        // revoke with invalid client
        val invalidTokenRevoke = UserRevokeTokenInput(accessToken, ANDROID_CLIENT_ID, null)
        doTokenRevoke(invalidTokenRevoke) {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.error") { value("unauthorized_client") }
        }

        // revoke with invalid secret
        val invalidTokenRevokeSecret = UserRevokeTokenInput(accessToken, WEB_CLIENT_ID, null)
        doTokenRevoke(invalidTokenRevokeSecret) {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.error") { value("invalid_client") }
        }

        // revoke
        val tokenRevoke = UserRevokeTokenInput(accessToken, WEB_CLIENT_ID, WEB_CLIENT_SECRET)
        doTokenRevoke(tokenRevoke) {
            status { is2xxSuccessful() }
        }
    }
}
