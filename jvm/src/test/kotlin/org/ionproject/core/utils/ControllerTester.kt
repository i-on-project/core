package org.ionproject.core.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.common.Media
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.net.URI

const val TEST_USER_AGENT = "ControllerTester"
const val WEB_CLIENT_NAME = "i-on Web"
const val WEB_CLIENT_ID = "22dd1551-db23-481b-acde-d286440388a5"
const val WEB_CLIENT_SECRET = "gntBY4mjX8PH4_5_i_H54fMFLl2x15Q0O4jWXodQ4aPmofF4i6VBf39tXi5vhdjA2WZ-5hwaOXAL11oibnZ8og"
const val ANDROID_CLIENT_NAME = "i-on Android"
const val ANDROID_CLIENT_ID = "14633a07-30d8-41f9-aa4d-d55341d7c7f3"
const val DUMMY_USER_ACCESS_TOKEN = "Bearer VaxiHhhn2HSsVBTlm7Ks3Lt5u7y9TUjIJ_3O1uyiuNG0RoQ7PECzqgQjnlOgZO2iMgJ7G76VU9MtOjcOYT9CYg"

var readTokenTest = "" // Used to allow tests to run or they will all fail with 400
var issueTokenTest = "" // Used to test the issue of tokens in AccessControlTest
var revokeTokenTest = "" // used to test the revoke endpoint

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
internal class ControllerTester {
    val jacksonMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mocker: MockMvc

    fun isValidSiren(uri: URI, actions: Boolean = false) = isValidSiren(uri, readTokenTest, actions)

    /**
     * Actions is not a mandatory component of the Siren representation
     * but it is necessary for some responses to test if they have actions
     */
    fun isValidSiren(uri: URI, authorization: String, actions: Boolean = false) = doGet(uri) {
        accept = Media.MEDIA_SIREN
        header(HttpHeaders.AUTHORIZATION, authorization)
    }.andExpect {
        status { isOk() }
        content { contentType("application/vnd.siren+json") }
        jsonPath("$.links") { exists() }

        if (actions)
            jsonPath("$.actions") { exists() }
    }

    fun doGet(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.get(uri) {
        header(HttpHeaders.USER_AGENT, TEST_USER_AGENT)
        dsl()
    }

    fun doPost(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.post(uri) {
        header(HttpHeaders.USER_AGENT, TEST_USER_AGENT)
        dsl()
    }

    fun doPut(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.put(uri) {
        header(HttpHeaders.USER_AGENT, TEST_USER_AGENT)
        dsl()
    }

    fun doDelete(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.delete(uri) {
        header(HttpHeaders.USER_AGENT, TEST_USER_AGENT)
        dsl()
    }
}
