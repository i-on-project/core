package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "${version}/courses/{cid}"
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val klasses = "${version}/courses/{cid}/classes"
    const val klassByTerm = "${version}/courses/{cid}/classes/{calterm}"
    const val classSectionById = "${version}/courses/{cid}/classes/{calterm}/{sid}"

    val courseByAcrTemplate = UriTemplate(courseByAcr)
    val programmesByIdTemplate = UriTemplate(programmesById)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)

    fun forCourses() = URI(courses)
    fun forProgrammesById(id : Int) = programmesByIdTemplate.expand(id)
    fun forCourseByAcr(cid: Int) = courseByAcrTemplate.expand(cid)
    fun forKlasses(cid: Int) = klassesTemplate.expand(cid)
    fun forKlassByTerm(cid: Int, calterm: String) = klassByTermTemplate.expand(cid, calterm)
    fun forClassSectionById(cid: Int, calterm: String, sid: String) = classSectionByIdTemplate.expand(cid, calterm, sid)
}