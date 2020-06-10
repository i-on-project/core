package org.ionproject.core.misc

import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Test
import java.net.URI

internal class InterceptorsErrorTest : ControllerTester() {
    companion object {
        val badQueryParamsRequest = URI("/v0/courses?page=1&limit=50")
        val goodQueryParamsRequest = URI("/v0/courses?page=0&limit=50") //Page 0 should always return empty list
        val excedingQueryParamsLimit = URI("/v0/courses?page=0&limit=500") //Limit is 100

        /**
         * No point in testing all endpoints that use
         * query parameters as they all use the same
         * interceptor to validate the parameters.
         */
    }

    @Test
    fun badQueryParamsTest() {
        doGet(badQueryParamsRequest) {
          header("Authorization", readToken)
        }
                .andDo { print() }
                .andExpect {
                    status {isNotFound}
                }.andReturn()
    }

    @Test
    fun goodQueryParamsTest() {
        doGet(goodQueryParamsRequest) {
          header("Authorization", readToken)
        }
                .andDo { print() }
                .andExpect {
                    status {isOk}
                }.andReturn()
    }

    @Test
    fun excedingQueryParamsLimits() {
        doGet(excedingQueryParamsLimit) {
          header("Authorization", readToken)
        }
                .andDo { print() }
                .andExpect {
                    status {isBadRequest}
                }.andReturn()
    }
}