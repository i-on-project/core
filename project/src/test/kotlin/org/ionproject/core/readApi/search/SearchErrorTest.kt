package org.ionproject.core.readApi.search

import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class SearchErrorTest : ControllerTester() {

    @Test
    fun searchWithoutQuery() {
        mocker.get("/v0/search?limit=2") {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }

    @Test
    fun searchWithInvalidType() {
        mocker.get("/v0/search?query=something&types=banana") {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }

    @Test
    fun searchWithInvalidQuerySyntax() {
        mocker.get("/v0/search?query=!") {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest }
            }
            .andReturn()
    }
}