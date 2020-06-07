package org.ionproject.core.home

import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Test
import org.springframework.test.web.client.match.MockRestRequestMatchers
import java.net.URI

internal class HomeDocumentTest : ControllerTester() {

    companion object {
        val expected = Home(
            Api(
                "i-on Core",
                "https://github.com/i-on-project/core/tree/master/docs/api"
            ),
            mapOf(
                "courses" to Resource(
                    "/v0/courses{?page,limit}",
                    mapOf(
                        "limit" to "/api-docs/params/limit",
                        "page" to "/api-docs/params/page"
                    ),
                    Hints(
                        listOf("GET"),
                        mapOf("application/vnd.siren+json" to mapOf<String, Any>()),
                        "https://github.com/i-on-project/core/tree/master/docs/api/courses.md"
                    )
                ),
                "calendar-terms" to Resource(
                    "/v0/calendar-terms{?page,limit}",
                    mapOf(
                        "limit" to "/api-docs/params/limit",
                        "page" to "/api-docs/params/page"
                    ),
                    Hints(
                        listOf("GET"),
                        mapOf("application/vnd.siren+json" to mapOf<String, Any>()),
                        "https://github.com/i-on-project/core/tree/master/docs/api/calendar-terms.md"
                    )
                )
            )
        )
    }

    @Test
    fun getHomeDocument() {
        doGet(URI.create("/")) {
          header("Authorization", read_token)
        }
            .andDo { print() }
            .andExpect {
                status { isOk }
                content { contentType("application/json-home") }
                jsonPath("$.api") {
                    exists()
                }
                jsonPath("$.resources") {
                    exists()
                }
                jsonPath("$.resources.courses") {
                    exists()
                }
                jsonPath("$.resources.calendar-terms") {
                    exists()
                }
                jsonPath("$") {
                    value(expected)
                }
            }
    }
}
