package org.ionproject.core.course

import org.ionproject.core.common.*
import org.ionproject.core.course.representations.courseToDetailRepr
import org.ionproject.core.course.representations.courseToListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/*
 * Course controller, accepts only application/json and vnd.siren+json,
 * should it accept any type and return an error in case there is no
 * representation?
 */
@RestController
class CourseController(private val courseServices: CourseServices){


    @GetMapping(Uri.courses, produces = [Media.SIREN_TYPE])
    fun getCourses(@RequestParam(defaultValue = "0") page : Int, @RequestParam(defaultValue = "10") limit : Int) : Siren =
        courseToListRepr(courseServices.getCourses(page, limit), page, limit)


    @GetMapping(Uri.courseById, produces = [Media.SIREN_TYPE])
    fun getCourse(@PathVariable cid: Int) : Siren =
            courseToDetailRepr(courseServices.getCourseById(cid))

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