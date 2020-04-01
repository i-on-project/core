package org.ionproject.core.course

import com.fasterxml.jackson.annotation.JsonCreator
import edu.isel.daw.project.common.EmbeddedEntity
import edu.isel.daw.project.common.SirenAction
import edu.isel.daw.project.common.SirenEntity
import edu.isel.daw.project.common.SirenLink
import org.ionproject.core.common.JSON_MEDIA_TYPE
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
 * TODO: MISSING CALENDAR AND EVENTS REPRESENTATION E NAS ACTIONS
 *  O ISTEMPLATED
 *  -ALSO N FALTA OS FIELDS DE EDIT?
 *  - +1920
 */
class CourseOutputModel(val acronym : String,
                        val name : String,
                        val calendarId : Int) {
    fun toSirenObject() = SirenEntity(
            clazz = listOf("course"),
            properties = this,
            entities = listOf(
                    EmbeddedEntity<IClass>(
                            clazz = listOf("class", "collection"),
                            rel = listOf("/rel/class"),
                            links = listOf(
                                    SirenLink(
                                            rel = listOf("self"),
                                            href = URI("/v0/courses/${acronym}/classes")
                                    ),
                                    SirenLink(
                                            rel = listOf("course"),
                                            href = URI("/v0/courses/${acronym}/")
                                    )
                            )
                    )
            ),
            actions = listOf(
                    SirenAction(
                            name = "delete",
                            title = "Delete Course",
                            method = HttpMethod.DELETE,
                            href = URI("/v0/courses/${acronym}")
                    ),
                    SirenAction(
                            name = "edit",
                            title = "Edit Course",
                            method = HttpMethod.PATCH,
                            type = MediaType.APPLICATION_JSON,
                            href = URI("/v0/courses/${acronym}")
                    )
            ),
            links = listOf(
                    SirenLink(
                            rel = listOf("self"),
                            href = URI("/v0/courses/${acronym}/")
                    ),
                    SirenLink(
                            rel = listOf("collection"),
                            href = URI("/v0/courses/")
                    )
            )
    )
}

class CoursesOutputModel(val courses : List<Course>) {
    fun toSirenObject() = SirenEntity(
            clazz = listOf("collection", "course"),
            properties = null,
            entities = courses.map {
                EmbeddedEntity<ICourse>(
                        clazz = listOf("course"),
                        rel = listOf("item"),
                        properties = it,
                        links = listOf(
                                SirenLink(
                                        rel = listOf("self"),
                                        href = URI("/v0/courses/${it.acronym}")
                                ),
                                SirenLink(
                                        rel = listOf("current"),
                                        href = URI("/v0/courses/${it.acronym}/classes/TODO")
                                ),
                                SirenLink(
                                        rel = listOf("collection"),
                                        href = URI("/v0/courses/")
                                )
                        )
                )
            },
            actions = listOf(
                    SirenAction(
                            name = "Add-Item",
                            title = "Add a new course",
                            method = HttpMethod.POST,
                            href = URI("/v0/courses"),
                            type = MediaType.APPLICATION_JSON
                    ),
                    SirenAction(
                            name = "Search",
                            title = "Search Items",
                            method = HttpMethod.GET,
                            href = URI("/v0/courses/"), //{?limit,page}
                            type = MediaType.APPLICATION_JSON,
                            fields = listOf(
                                    SirenAction.Field(
                                            name = "limit",
                                            type = "number"
                                    ),
                                    SirenAction.Field(
                                            name = "page",
                                            type = "number"
                                    )
                            )
                    )
            ),
            links = listOf(
                    SirenLink(
                            rel = listOf("self"),
                            href = URI("/courses?page=1&limit=2")
                    )
            )
    )
}