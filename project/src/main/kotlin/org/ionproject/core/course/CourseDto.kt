package org.ionproject.core.course

import com.fasterxml.jackson.annotation.JsonCreator
import edu.isel.daw.project.common.EmbeddedEntity
import edu.isel.daw.project.common.SirenAction
import edu.isel.daw.project.common.SirenEntity
import edu.isel.daw.project.common.SirenLink
import org.ionproject.core.common.*
import org.ionproject.core.common.model.Course
import org.ionproject.core.common.modelInterfaces.ICourse
import org.ionproject.core.course_instance.IClass
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.lang.Exception
import java.net.URI

class InvalidCourseException() : Exception()

class CourseInputModel @JsonCreator constructor(val acronym : String,
                                                val name : String,
                                                val calendarId : Int) {
    fun toCourse() = Course(acronym, name, calendarId) ?: throw InvalidCourseException()
}

/*
 * TODO: TODOYEAR
 *  OBJETO URI EXPLODE COM "/v0/courses{?limit,page}"
 */


class CourseOutputModel(val acronym : String,
                        val name : String,
                        val calendarId : Int) {
    fun toSirenObject() = SirenBuilder(this)
            .klass("course")
            .entities(listOf(buildSubentities(acronym, name, calendarId)))
            .action(Action(name = "delete", title = "delete course", method = HttpMethod.DELETE,
                    href = URI("${COURSES_PATH}${acronym}"), isTemplated = true))
            .action(Action(name = "edit", title = "edit course", method = HttpMethod.PATCH,
                    href = URI("${COURSES_PATH}${acronym}"), isTemplated = false))
            .toSiren()


    private fun buildSubentities(acronym : String,
                                 name : String,
                                 calendarId : Int) : EmbeddedRepresentation = SirenBuilder()
            .klass("class", "collection")
            .rel(REL_CLASS)
            .link("self", URI("${COURSES_PATH}${acronym}/${CLASS_ENTITY}"))
            .link("course", URI("${COURSES_PATH}${acronym}/"))
            .toEmbed()
}

class CoursesOutputModel(val courses : List<ICourse>) {
    fun toSirenObject() = SirenBuilder()
            .klass("course", "collection")
            .entities(
                    courses.map {
                        buildSubentities(it)
                    }
            )
            .action(Action(name = "add-item", title = "Add a new Course", method = HttpMethod.POST,
                    href = URI(COURSES_PATH), isTemplated = false, type = JSON_MEDIA_TYPE))
            .action(Action(name = "search", title = "Search Items", method = HttpMethod.GET,
                    href = URI("${COURSES_PATH}limitpage"), isTemplated = true, type = SIREN_MEDIA_TYPE,
                    fields = listOf(
                            Field(name = "limit", type = "number", klass = "param/limit"),
                            Field(name = "page", type = "number", klass = "param/page")
                            )
            )).toSiren()

    private fun buildSubentities(course : ICourse) : EmbeddedRepresentation = SirenBuilder(course)
            .klass("course")
            .rel("item")
            .link("self", URI("${COURSES_PATH}/${course.acronym}"))
            .link("current", URI("${COURSES_PATH}/${course.acronym}/${CLASS_ENTITY}/TODOYEAR"))
            .link("collection", URI(COURSES_PATH))
            .toEmbed()

}
