package org.ionproject.core.common

import org.springframework.web.util.UriTemplate
import java.net.URI

object Uri {
    private const val version = "/v0"

    const val courses = "${version}/courses"
    const val courseByAcr = "${version}/courses/{acr}"
    const val programmes = "${version}/programmes"
    const val programmesById = "${version}/programmes/{id}"
    const val klasses = "${version}/courses/{acr}/classes"
    const val klassByTerm = "${version}/courses/{acr}/classes/{calterm}"
    const val classSectionById = "${version}/courses/{acr}/classes/{calterm}/{id}"

    val courseByAcrTemplate = UriTemplate(courseByAcr)
    val programmesByIdTemplate = UriTemplate(programmesById)
    val klassesTemplate = UriTemplate(klasses)
    val klassByTermTemplate = UriTemplate(klassByTerm)
    val classSectionByIdTemplate = UriTemplate(classSectionById)

    fun forCourses() = URI(courses)
    fun forProgrammesById(id : Int) = programmesByIdTemplate.expand(id)
    fun forCourseByAcr(acr: String) = courseByAcrTemplate.expand(acr)
    fun forKlasses(acr: String) = klassesTemplate.expand(acr)
    fun forKlassByTerm(acr: String, calterm: String) = klassByTermTemplate.expand(acr, calterm)
    fun forClassSectionById(acr: String, calterm: String, id: String) = classSectionByIdTemplate.expand(acr, calterm, id)
}