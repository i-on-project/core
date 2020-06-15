package org.ionproject.core.accessControl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.representations.TokenRepr
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.issueTokenTest
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.*
import java.net.URI

internal class AccessControlTest: ControllerTester() {

    companion object {
        /**
         * This token only allows to GET from "/programmes" and "/programmes/{id}"
         */
        private const val readRestrictedToken = "ODY0SXpvcnlHYUtJbmFsZ1RyM0pvakJQM3oxYk00dUQ="
        private const val includeBearer = "Bearer"
        private const val includeNotBearer = "notBearer"

        /**
         * Invalid tokens
         */
        const val unexistentToken = "$includeBearer lol"
        const val revokedToken = "$includeBearer S3A5OENndnozaVVTcEttMTI4MjJ0UlUwb3h3QmRDeHc="
        const val expiredToken = "$includeBearer czIxaklCckk0VjZSUjdhajF2WTkzb1JNUlNhY0FGWmU="

        /**
         * Valid tokens (one of them has a wrong include type)
         */
        const val tokenCorrect = "$includeBearer $readRestrictedToken"
        const val tokenIncorrect = "$includeNotBearer $readRestrictedToken"

        /**
         * Uri's for testing according to the tokens scopes
         */
        val notFoundUri = URI("somethingnotfound")
        val programmesUri = Uri.forProgrammes()
        val programmesDetailUri = Uri.forProgrammesById(1)
        val programmesQueryParamsUri = URI("/v0/programmes?page=1&limit=1")
        val coursesUri = Uri.forCourses()
        val homeDocumentUri = URI("/")
        val issueTokenUri = URI(Uri.issueToken)
        val revokeTokenUri = URI(Uri.revokeToken)

    }
    /**
     * Because all requests pass through the access control interceptor
     * I believe its important to test if the behavior
     * of the not found rejected page is kept...
     */
    @Test
    fun getNotFoundPage() {
        doGet(notFoundUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isNotFound } }
                .andReturn()
    }

    /**
     * Testing special case home document
     * because it doesn't have the api version component
     * and the policies had to be adapted to allow it.
     */
    @Test
    fun getHomeDocument() {
        doGet(homeDocumentUri) {
            header("Authorization", readTokenTest)
        }
                .andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
    }

    /**
     * Get programmes, with read_restricted token,
     * for security reasons (being exposed here) this token only allows a GET
     * on "/programmes" and "/programmes/{id}".
     */
    @Test
    fun getProgrammes() {
        doGet(programmesUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
    }

    @Test
    fun getProgrammesDetail() {
        doGet(programmesDetailUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
    }

    @Test
    fun getProgrammesQueryParameter() {
        doGet(programmesQueryParamsUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
    }

    /**
     * Tries to read from resource courses with the
     * restricted to programmes token.
     */
    @Test
    fun getCourses() {
        doGet(coursesUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isForbidden } }
                .andReturn()
    }

    /**
     * Get courses with unexistent token
     */
    @Test
    fun getCoursesWithUnexistentToken() {
        doGet(coursesUri) {
            header("Authorization", unexistentToken)
        }
                .andDo { print() }
                .andExpect { status { isUnauthorized } }
                .andReturn()
    }

    /**
     * Get courses with revoked token
     */
    @Test
    fun getCoursesWithRevokedToken() {
        doGet(coursesUri) {
            header("Authorization", revokedToken)
        }
                .andDo { print() }
                .andExpect { status { isUnauthorized } }
                .andReturn()
    }

    /**
     * Get courses with expired token
     */
    @Test
    fun getCoursesWithExpiredToken() {
        doGet(coursesUri) {
            header("Authorization", expiredToken)
        }
                .andDo { print() }
                .andExpect { status { isUnauthorized } }
                .andReturn()
    }

    /**
     * POST to /programmes with read_restricted token
     */
    @Test
    fun postProgrammesWithReadToken() {
        doPost(programmesUri) {
            header("Authorization", tokenCorrect)
        }
                .andDo { print() }
                .andExpect { status { isForbidden } }
                .andReturn()
    }

    /**
     * GET programmes with an unsupported include type token
     * e.g. something else besides 'Bearer'
     */
    @Test
    fun getProgrammesUnsupportedIncludeToken() {
        doGet(programmesUri) {
            header("Authorization", tokenIncorrect)
        }
                .andDo { print() }
                .andExpect { status { isBadRequest } }
                .andReturn()
    }


    /**
     * Test issue token and then revoke it
     */
    @Test
    fun issueTokenAndRevoke() {
        val result = doPost(issueTokenUri) {
            header("Authorization", issueTokenTest)
            header("Content-Type", "application/json")

            content = "{\"scope\":\"urn:org:ionproject:scopes:api:read\"}"
        }
                .andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
                .response
                .contentAsString

        val mapper = jacksonObjectMapper()
        val tokenToRevoke = mapper.readValue(result, TokenRepr::class.java).token

        doPost(revokeTokenUri) {
            header("Authorization", "Bearer $tokenToRevoke")
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=$tokenToRevoke"
        }.andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
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
        }
                .andDo { print() }
                .andExpect { status { isUnauthorized } }
                .andReturn()
    }

    /**
     * Tries to revoke an invalid token
     * the answer should be 200 OK as specified by [RFC 7009]
     */
    @Test
    fun revokeTokenInvalid() {
        doPost(revokeTokenUri) {
            header("Authorization", readTokenTest)
            contentType = Media.MEDIA_FORM_URLENCODED_VALUE
            content = "token=ggdsiojgfsdfioj"
        }.andDo { print() }
                .andExpect { status { isOk } }
                .andReturn()
    }

}