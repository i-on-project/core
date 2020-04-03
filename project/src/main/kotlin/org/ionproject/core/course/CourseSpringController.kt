package org.ionproject.core.course

import edu.isel.daw.project.common.SirenEntity
import org.ionproject.core.common.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/*
 * Course controller, accepts only application/json and vnd.siren+json,
 * should it accept any type and return an error in case there is no
 * representation?
 */
@RestController
class CourseSpringController(private val courseServices: CourseServices){

    @GetMapping(COURSES_PATH)
    @ResponseStatus(HttpStatus.OK)
    fun getCourses() : Siren {
        val courses = courseServices.getCourses()
        return CoursesOutputModel(courses).toSirenObject()
    }

    @GetMapping(COURSES_PATH_ACR)
    @ResponseStatus(HttpStatus.OK)
    fun getCourse(@PathVariable acr: String) : Siren {
        val course = courseServices.getCourse(acr)
        return CourseOutputModel(course.acronym,
                course.name,
                course.calendarId).toSirenObject()
    }

    /*
     * Annotation `RequiresAuth` serves as an indication
     * that to use this endpoint credentials must be
     * provided.
     */
    @DeleteMapping(COURSES_PATH_ACR)
    @RequiresAuthentication
    fun deleteCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    @PatchMapping(COURSES_PATH_ACR)
    @RequiresAuthentication
    fun editCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    //TODO: Siren Action search items implementation com query params

    //Em caso de status code 406 retornar problem json com body
}