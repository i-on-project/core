package org.ionproject.core.home

import org.ionproject.core.utils.ControllerTester
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI

internal class HomeDocumentTest : ControllerTester() {
  companion object {
    val expected = """{
  "api" : {
    "title" : "i-on Core",
    "links" : {
      "describedBy" : "https://github.com/i-on-project/core/tree/master/docs/api"
    }
  },
  "resources" : {
    "courses" : {
      "hrefTemplate" : "/v0/courses{?page,limit}",
      "hrefVars" : {
        "limit" : "/api-docs/params/limit",
        "page" : "/api-docs/params/page"
      },
      "hints" : {
        "allow" : [ "GET" ],
        "formats" : {
          "application/vnd.siren+json" : { }
        },
        "docs" : "https://github.com/i-on-project/core/tree/master/docs/api/courses.md"
      }
    },
    "calendar-terms" : {
      "hrefTemplate" : "/v0/calendar-terms{?page,limit}",
      "hrefVars" : {
        "limit" : "/api-docs/params/limit",
        "page" : "/api-docs/params/page"
      },
      "hints" : {
        "allow" : [ "GET" ],
        "formats" : {
          "application/vnd.siren+json" : { }
        },
        "docs" : "https://github.com/i-on-project/core/tree/master/docs/api/calendar-terms.md"
      }
    }
  }
}"""
  }

  @Test
  fun getHomeDocument() {
    val body = doGet(URI.create("/"))
      .andDo { print() }
      .andExpect {
        status { isOk }
        content { contentType("application/json-home") }
        jsonPath("$.api") { exists() }
        jsonPath("$.resources") { exists() }
        jsonPath("$.resources.courses") { exists() }
        jsonPath("$.resources.calendar-terms") { exists() }
      }
      .andReturn()
      .response
      .contentAsString

    Assertions.assertEquals(expected, body)
  }
}
