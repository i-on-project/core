package org.ionproject.core.search

import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class SearchErrorTest : ControllerTester() {

    @Test
    fun searchWithoutQuery() {
        mocker.get("/api/search?limit=2") {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }

    @Test
    fun searchWithInvalidType() {
        mocker.get("/api/search?query=something&types=banana") {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }

    @Test
    fun searchWithInvalidQuerySyntax() {
        mocker.get("/api/search?query=!") {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }
}
