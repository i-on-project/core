package org.ionproject.core.utils

import org.ionproject.core.common.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.client.match.MockRestRequestMatchers.header
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.io.File
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
internal class ControllerTester {
    @Autowired
    lateinit var mocker: MockMvc

    /**
     * This is the same token as the one used by the android client,
     * its read from a file to not leak it. (It may be unnecessary as the token will be present in the APK anyways)
     */
    val read_token = "Bearer "+ loadReadToken()

    private final fun loadReadToken(): String {
        return File("read_token.txt").readLines()[0]
    }

    fun isValidSiren(uri: URI) = mocker.get(uri) {
        accept = Media.MEDIA_SIREN
        header("Authorization", read_token)
    }.andExpect {
        status { isOk }
        content { contentType("application/vnd.siren+json") }
        jsonPath("$.links") { exists() }
        jsonPath("$.actions") { exists() }
    }

    fun doGet(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.get(uri, dsl)

    fun doPost(uri: URI, dsl: MockHttpServletRequestDsl.() -> Unit = {}) = mocker.post(uri, dsl)

}

