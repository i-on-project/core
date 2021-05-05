package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.representations.ImportLinkRepr
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.issueTokenTest
import org.ionproject.core.utils.readTokenTest
import org.ionproject.core.utils.revokeTokenTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URI

internal class RevokeTokenTests : ControllerTester() {

    @Autowired
    lateinit var cache: AccessControlCache

    companion object {
        val revokeTokenUri = URI(Uri.revokeToken)
        val issueTokenUri = URI(Uri.issueToken)
        val importClassSectionCalendarUrl = Uri.forImportClassSectionCalendar(5, "1920v", "LI61D")
    }

    private fun issueTokenTest(scope: String): String {
        val result = doPost(issueTokenUri) {
            header("Authorization", issueTokenTest)
            header("Content-Type", "application/json")

            content = "{\"scope\":\"$scope\"}"
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
        val tokenToRevoke = issueTokenTest("urn:org:ionproject:scopes:api:revoke")
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
            header("Authorization", issueTokenTest)
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
            header("Authorization", issueTokenTest)
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
            header("Authorization", issueTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$token&operation=3"
        }.andDo { print() }
            .andExpect { status { isForbidden } }
            .andReturn()
    }

    /**
     * Tries to revoke child and father tokens
     */
    @Test
    fun revokeTokenChildAndFather() {
        // Issuing father token
        val fatherTokenToRevoke = issueTokenTest("urn:org:ionproject:scopes:api:read")

        // Issuing a child token
        val linkResult = doGet(importClassSectionCalendarUrl) {
            header("Authorization", "Bearer $fatherTokenToRevoke")
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        // Checking if the issued import link is valid
        val jsonLink = convertToJson(linkResult)
        val link = jsonLink.url.dropWhile { c -> c != '/' }
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } } // If the child token is revoked it should answer with 401
            .andReturn()

        // Revoking father and child tokens
        doPost(revokeTokenUri) {
            header("Authorization", revokeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$fatherTokenToRevoke&operation=3"
        }.andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

        cache.clearCache()

        // Checking if son token is revoked
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isUnauthorized } } // If the child token is revoked it should answer with 401
            .andReturn()

        // Checking if father token is revoked by trying issue another token
        doGet(importClassSectionCalendarUrl) {
            header("Authorization", "Bearer $fatherTokenToRevoke")
        }
            .andDo { print() }
            .andExpect { status { isUnauthorized } }
            .andReturn()
    }

    /**
     * Revoke child without revoking father
     */
    @Test
    fun revokeChildNoRevokeFather() {
        val fatherToken = issueTokenTest("urn:org:ionproject:scopes:api:read")

        // Issuing a child token
        val linkResult = doGet(importClassSectionCalendarUrl) {
            header("Authorization", "Bearer $fatherToken")
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        // Checking if the issued import link is valid
        val jsonLink = convertToJson(linkResult)
        // val link = jsonLink.url.dropWhile { c -> c != '/' }
        val link = jsonLink.url
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } } // If the child token is revoked it should answer with 401
            .andReturn()

        // Revoking child no revoke father
        doPost(revokeTokenUri) {
            header("Authorization", revokeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$fatherToken&operation=1"
        }.andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

        cache.clearCache()

        // Check if child is revoked
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isUnauthorized } } // If the child token is revoked it should answer with 401
            .andReturn()

        // Check if the father was not revoked
        doGet(URI("/")) {
            header("Authorization", "Bearer $fatherToken")
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
    }

    /**
     * Revoke father without revoking child
     */
    @Test
    fun revokeFatherNoRevokeChild() {
        val fatherToken = issueTokenTest("urn:org:ionproject:scopes:api:read")

        // Issuing a child token
        val linkResult = doGet(importClassSectionCalendarUrl) {
            header("Authorization", "Bearer $fatherToken")
        }
            .andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()
            .response
            .contentAsString

        // Checking if the issued import link is valid
        val jsonLink = convertToJson(linkResult)
        val link = jsonLink.url.dropWhile { c -> c != '/' }
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } } // If the child token is revoked it should answer with 401
            .andReturn()

        // Revoking father no revoke child
        doPost(revokeTokenUri) {
            header("Authorization", revokeTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$fatherToken&operation=2"
        }.andDo { print() }
            .andExpect { status { isOk } }
            .andReturn()

        cache.clearCache()

        // Check if the father was revoked
        doGet(URI(Uri.apiBase)) {
            header("Authorization", "Bearer $fatherToken")
        }
            .andDo { print() }
            .andExpect { status { isUnauthorized } }
            .andReturn()

        // Check if the issued import url is valid
        doGet(URI(link)) {
        }
            .andDo { print() }
            .andExpect { status { isOk } } // If the child token is revoked it should answer with 401
            .andReturn()
    }

    private fun convertToJson(result: String): ImportLinkRepr {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(result, ImportLinkRepr::class.java)
    }
}
