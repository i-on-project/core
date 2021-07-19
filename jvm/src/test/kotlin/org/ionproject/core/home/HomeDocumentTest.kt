package org.ionproject.core.home

import org.ionproject.core.common.Uri
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
                    "${Uri.baseUrl}/api/courses{?page,limit}",
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
                    "${Uri.baseUrl}/api/calendar-terms{?page,limit}",
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
                    "${Uri.baseUrl}/api/search{?query,types,limit,page}",
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
                    "${Uri.baseUrl}/api/programmes{?page,limit}",
                    mapOf(
                        "limit" to "/api-docs/params/limit",
                        "page" to "/api-docs/params/page"
                    ),
                    Hints(
                        listOf("GET"),
                        mapOf("application/vnd.siren+json" to mapOf<String, Any>()),
                        "https://github.com/i-on-project/core/tree/master/docs/api/programme.md"
                    )
                ),
                "revokeToken" to Resource(
                    "${Uri.baseUrl}/api/revokeToken",
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
        doGet(URI.create(Uri.apiBase)) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isOk() }
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
                jsonPath("$.resources.programmes") {
                    exists()
                }
                jsonPath("$.resources.revokeToken") {
                    exists()
                }
                jsonPath("$") {
                    value(expected)
                }
            }
    }
}
