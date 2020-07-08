package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.*
import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Test
import java.net.URI

internal class RevokeTokenTests : ControllerTester() {

    companion object {
        val revokeTokenUri = URI(Uri.revokeToken)
        val issueTokenUri = URI(Uri.issueToken)
    }

    private fun issueTokenTest() : String {
        val result = doPost(issueTokenUri) {
            header("Authorization", issueTokenTest)
            header("Content-Type", "application/json")

            content = "{\"scope\":\"urn:org:ionproject:scopes:api:write\"}"
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        val mapper = jacksonObjectMapper()
        return mapper.readValue(result, TokenRepr::class.java).token
    }

    /**
     * Test issue token and then revoke it
     */
    @Test
    fun issueTokenAndRevoke() {
        val tokenToRevoke = issueTokenTest()
        doPost(revokeTokenUri) {
            header("Authorization", "Bearer $tokenToRevoke")
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$tokenToRevoke"
        }.andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
    }

    /**
     * Tries to revoke a read token with a read token, which shall not succeed
     */
    @Test
    fun tryRevokeReadToken() {
        doPost(revokeTokenUri) {
            header("Authorization", readTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$readTokenTest"
        }.andDo { print() }
            .andExpect { status { isForbidden } }
            .andReturn()
    }

    /**
     * Tries to revoke a read token, using a write token, which is a forbidden operation
     */
    @Test
    fun tryRevokeReadTokenUsingWriteToken() {
        val token = readTokenTest.split(" ")[1]
        doPost(revokeTokenUri) {
            header("Authorization", writeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$token"
        }.andDo { print() }
            .andExpect { status { isForbidden } }
            .andReturn()
    }

    /**
     * Tries to revoke an token with a bad request (no body)
     */
    @Test
    fun revokeTokenBadRequest() {
        doPost(revokeTokenUri) {
            header("Authorization", writeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
        }.andDo { print() }
            .andExpect { status { isBadRequest } }
            .andReturn()
    }

    /**
     * Tries a privileged revoke with an unprivileged token
     */
    @Test
    fun revokeTokenChildAndFatherUnprivilegedToken() {
        val token = readTokenTest.split(" ")[1]

        doPost(revokeTokenUri) {
            header("Authorization", writeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$token&operation=3"

        }.andDo { print() }
            .andExpect { status { isForbidden } }
            .andReturn()
    }
}
