package org.ionproject.core.utils

import org.ionproject.core.common.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*
import java.net.URI

var readTokenTest = ""  //Used to allow tests to run or they will all fail with 400
var writeTokenTest = ""
var issueTokenTest = "" //Used to test the issue of tokens in AccessControlTest
var revokeTokenTest = "" //used to test the revoke endpoint

@SpringBootTest
@AutoConfigureMockMvc
internal class ControllerTester {
    @Autowired
    lateinit var mocker: MockMvc

    /**
     * Actions is not a mandatory component of the Siren representation
     * but it is necessary for some responses to test if they have actions
     */
    fun isValidSiren(uri: URI, actions: Boolean = false) = mocker.get(uri) {
        accept = Media.MEDIA_SIREN
        header("Authorization", readTokenTest)
    }.andExpect {
        status { isOk }
        content { contentType("application/vnd.siren+json") }
        jsonPath("$.links") { exists() }

        if(actions)
            jsonPath("$.actions") { exists() }
    }


    fun doGet(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.get(uri, dsl)

    fun doPost(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.post(uri, dsl)

    fun doPut(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.put(uri, dsl)
}

