package org.ionproject.core.search

import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class SearchErrorTest : ControllerTester() {

    @Test
    fun searchWithoutQuery() {
        mocker.get("/v0/search?limit=2")
            .andDo { print() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }

    @Test
    fun searchWithInvalidType() {
        mocker.get("/v0/search?query=something&types=banana")
            .andDo { print() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }
}