package org.ionproject.core.course

import org.ionproject.core.common.*
import org.ionproject.core.course.representations.courseToDetailRepr
import org.ionproject.core.course.representations.courseToListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/*
 * Course controller, accepts only application/json and vnd.siren+json,
 * should it accept any type and return an error in case there is no
 * representation?
 */
@RestController
class CourseSpringController(private val courseServices: CourseServices){


    @GetMapping(Uri.courses)
    fun getCourses() : Siren = courseToListRepr(
            courseServices.getCourses()
    )

    @GetMapping(Uri.courseByAcr)
    fun getCourse(@PathVariable acr: String) : ResponseEntity<Siren> =
            courseServices.getCourseByAcr(acr)
                    ?.let { ResponseEntity.ok(courseToDetailRepr(it))}
                    ?: ResponseEntity.notFound().build()

    /*
     * Annotation `RequiresAuth` serves as an indication
     * that to use this endpoint credentials must be
     * provided.
     */
    @DeleteMapping(Uri.courseByAcr)
    @RequiresAuthentication
    fun deleteCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    @PatchMapping(Uri.courseByAcr)
    @RequiresAuthentication
    fun editCourse(@PathVariable acr: String) {
        TODO("Waiting write API")
    }

    //TODO: Siren Action search items implementation com query params

    //Em caso de status code 406 retornar problem json com body
}