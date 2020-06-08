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
     * This is the same token as the one used by the android client and its public, for extra safety
     * it could be read from a file but it would fail the github CI/CD tests.
     */
    val read_token = "Bearer OHdZS3dhUVZtdTdzSlBHcVlUdXFrS0tNVmhQOW5PcXg="

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

