package org.ionproject.core.home

import org.ionproject.core.common.Media
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Uri
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private const val specLocation = "https://github.com/i-on-project/core/tree/master/docs/api"
private const val amSpecLocation = "https://github.com/i-on-project/core/tree/master/docs/access_control"

private val specUri = URI(specLocation)
private val coursesSpecUri = URI("$specLocation/courses.md")
private val calendarTermsSpecUri = URI("$specLocation/calendar-terms.md")
private val searchSpecUri = URI("$specLocation/search.md")
private val programmesSpecUri = URI("$specLocation/programme.md")
private val accessManagerSpecUri = URI("$amSpecLocation/Http_Exchanges.md")

private const val apiName = "i-on Core"

@RestController
class JsonHomeController {

    @ResourceIdentifierAnnotation(ResourceIds.GET_HOME, ResourceIds.VERSION_0)
    @GetMapping("/")
    fun getRoot(): ResponseEntity<JsonHome> {
        val homeObject = JsonHomeBuilder(apiName)
            .link("describedBy", specUri)
            // courses resource
            .newResource("courses") {
                it
                    .hrefTemplate(Uri.pagingCourses)
                    .hrefVar("limit", URI("/api-docs/params/limit"))
                    .hrefVar("page", URI("/api-docs/params/page"))
                    .docs(coursesSpecUri)
                    .formats(Media.MEDIA_SIREN)
                    .allow(HttpMethod.GET)
                    .toResourceObject()
            }
            // calendar terms resource
            .newResource("calendar-terms") {
                it
                    .hrefTemplate(Uri.pagingCalendarTerms)
                    .hrefVar("limit", URI("/api-docs/params/limit"))
                    .hrefVar("page", URI("/api-docs/params/page"))
                    .docs(calendarTermsSpecUri)
                    .formats(Media.MEDIA_SIREN)
                    .allow(HttpMethod.GET)
                    .toResourceObject()
            }
            .newResource("search") {
                it
                    .hrefTemplate(Uri.pagingSearch)
                    .hrefVar("query", URI("/api-docs/params/query"))
                    .hrefVar("types", URI("/api-docs/params/types"))
                    .hrefVar("limit", URI("/api-docs/params/limit"))
                    .hrefVar("page", URI("/api-docs/params/page"))
                    .docs(searchSpecUri)
                    .formats(Media.MEDIA_SIREN)
                    .allow(HttpMethod.GET)
                    .toResourceObject()
            }
            .newResource("programmes") {
                it
                    .href(Uri.forProgrammes())
                    .docs(programmesSpecUri)
                    .formats(Media.MEDIA_SIREN)
                    .allow(HttpMethod.GET)
                    .toResourceObject()
            }
            .newResource("revokeToken") {
                it
                    .href(URI(Uri.revokeToken))
                    .docs(accessManagerSpecUri)
                    .formats(Media.MEDIA_FORM_URLENCODED_VALUE)
                    .allow(HttpMethod.POST)
                    .toResourceObject()
            }
            .toJsonHome()

        return ResponseEntity.ok(homeObject)
    }
}
