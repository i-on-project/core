package org.ionproject.core.course

import edu.isel.daw.project.common.SirenEntity
import org.ionproject.core.common.V0_COURSES
import org.ionproject.core.common.V0_COURSES_ACR
import org.ionproject.core.common.authentication.RequiresAuth
import org.springframework.web.bind.annotation.*

/*
 * Course controller, accepts only application/json and vnd.siren+json,
 * should it accept any type and return an error in case there is no
 * representation?
 */
@RestController
@RequestMapping(V0_COURSES, headers =
            [ "Accept=application/json", "Accept=application/vnd.siren+json"])
class CourseSpringController(private val courseController: CourseController){

    @GetMapping
    fun getCourses() : SirenEntity<Nothing> {
        val courses = courseController.getCourses()
        return CoursesOutputModel(courses).toSirenObject()
    }

    @GetMapping(V0_COURSES_ACR)
    fun getCourse(@PathVariable acr: String) : SirenEntity<CourseOutputModel> {
        val course = courseController.getCourse(acr)
        return CourseOutputModel(course.acronym,
                course.name,
                course.calendarId).toSirenObject()
    }

    /*
     * Annotation `RequiresAuth` serves as an indication
     * that to use this endpoint credentials must be
     * provided.
     */
    @DeleteMapping(V0_COURSES_ACR)
    @RequiresAuth
    fun deleteCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    @PatchMapping(V0_COURSES_ACR)
    @RequiresAuth
    fun editCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    //TODO: Siren Action search items implementation com query params
}