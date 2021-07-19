package org.ionproject.core.accessControl

import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.issueTokenTest
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class IssueTokenTests : ControllerTester() {

    companion object {
        val issueTokenUri = URI(Uri.issueToken)
    }

    /**
     * Tries to issue a token, with an invalid token
     */
    @Test
    fun postIssueTokenInvalid() {
        doPost(issueTokenUri) {
            header("Authorization", "Bearer lol")
            header("Content-Type", "application/json")

            content = "{\"scope\":\"urn:org:ionproject:scopes:api:read\"}"
        }.andDo { println() }
            .andExpect { status { isUnauthorized() } }
            .andReturn()
    }

    /**
     * Tries to issue a token with an invalid scope
     */
    @Test
    fun issueInvalidScopeToken() {
        doPost(issueTokenUri) {
            header("Authorization", "Bearer $issueTokenTest")
            header("Content-Type", "application/json")

            content = "{\"scope\":\"whatkindofcrazyscopeisthis\"}"
        }.andDo { println() }
            .andExpect { status { isBadRequest() } }
            .andReturn()
    }

    /**
     * Tries to issue a token without body
     */
    @Test
    fun issueInvalidBodyToken() {
        doPost(issueTokenUri) {
            header("Authorization", "Bearer $issueTokenTest")
            header("Content-Type", "application/json")
        }.andDo { println() }
            .andExpect { status { isBadRequest() } }
            .andReturn()
    }

    /**
     * Tries to issue a token with an non-authorized token
     */
    @Test
    fun issueNotAuthorizedToken() {
        doPost(issueTokenUri) {
            header("Authorization", readTokenTest)
            header("Content-Type", "application/json")

            content = "{\"scope\":\"urn:org:ionproject:scopes:api:read\"}"
        }.andDo { println() }
            .andExpect { status { isForbidden() } }
            .andReturn()
    }
}
