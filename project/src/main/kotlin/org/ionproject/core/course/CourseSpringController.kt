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


    @GetMapping(Uri.courses)    //TODO LACKS TEMPLATING AND QUERY PARAMS
    fun getCourses() : Siren = courseToListRepr(
            courseServices.getCourses()
    )

    @GetMapping(Uri.courseById)
    fun getCourse(@PathVariable id: Int) : ResponseEntity<Siren> =
            courseServices.getCourseById(id)
                    ?.let { ResponseEntity.ok(courseToDetailRepr(it))}
                    ?: ResponseEntity.notFound().build()

    /*
     * Annotation `RequiresAuth` serves as an indication
     * that to use this endpoint credentials must be
     * provided.
     */
    @DeleteMapping(Uri.courseById)
    @RequiresAuthentication
    fun deleteCourse(@PathVariable id: Int) {
        TODO("Waiting write API")
    }

    @PatchMapping(Uri.courseById)
    @RequiresAuthentication
    fun editCourse(@PathVariable id: Int) {
        TODO("Waiting write API")
    }

}