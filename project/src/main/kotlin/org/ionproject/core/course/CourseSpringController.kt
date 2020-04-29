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
class CourseSpringController(private val courseServices: CourseServices){


    @GetMapping(Uri.courses, produces = [Media.SIREN_TYPE])
    fun getCourses(@RequestParam(defaultValue = "0") page : Int, @RequestParam(defaultValue = "0") limit : Int) : ResponseEntity<Siren> {
        val defaultFlag = page == 0 && limit == 0

        return courseServices.getCourses(page, limit, defaultFlag)
                .let {
                    ResponseEntity.ok()
                            .header("Content-Type", Media.SIREN_TYPE)
                            .body(courseToListRepr(it, page, limit))
                }
    }

    @GetMapping(Uri.courseById, produces = [Media.SIREN_TYPE])
    fun getCourse(@PathVariable cid: Int) : ResponseEntity<Siren> =
            courseServices.getCourseById(cid)
                    .let {
                        ResponseEntity.ok()
                                .header("Content-Type", Media.SIREN_TYPE)
                                .body(courseToDetailRepr(it))
                    }

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