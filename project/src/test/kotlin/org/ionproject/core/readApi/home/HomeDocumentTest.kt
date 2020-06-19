package org.ionproject.core.readApi.home

import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
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
                ),
                "search" to Resource(
                    "/v0/search{?query,types,limit,page}",
                    mapOf(
                        "query" to "/api-docs/params/query",
                        "types" to "/api-docs/params/types",
                        "limit" to "/api-docs/params/limit",
                        "page" to "/api-docs/params/page"
                    ),
                    Hints(
                        listOf("GET"),
                        mapOf("application/vnd.siren+json" to mapOf<String, Any>()),
                        "https://github.com/i-on-project/core/tree/master/docs/api/search.md"
                    )
                ),
                "programmes" to Resource(
                    "/v0/programmes",
                     Hints(
                        listOf("GET"),
                        mapOf("application/vnd.siren+json" to mapOf<String, Any>()),
                    "https://github.com/i-on-project/core/tree/master/docs/api/programme.md"
                    )
                ),
                "issueToken" to Resource(
                    "/issueToken",
                    Hints(
                            listOf("POST"),
                            mapOf("application/json" to mapOf<String, Any>()),
                            "https://github.com/i-on-project/core/tree/master/docs/access_control/Http_Exchanges.md"
                    )
                ),
                "revokeToken" to Resource(
                    "/revokeToken",
                    Hints(
                            listOf("POST"),
                            mapOf("application/x-www-form-urlencoded" to mapOf<String, Any>()),
                            "https://github.com/i-on-project/core/tree/master/docs/access_control/Http_Exchanges.md"
                    )
                )
            )
        )
    }


    @Test
    fun getHomeDocument() {
        doGet(URI.create("/")) {
          header("Authorization", readTokenTest)
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
                jsonPath("$.resources.programmes"){
                    exists()
                }
                jsonPath("$.resources.issueToken"){
                    exists()
                }
                jsonPath("$.resources.revokeToken"){
                    exists()
                }
                jsonPath("$") {
                    value(expected)
                }
            }
    }
}
