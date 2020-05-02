package org.ionproject.core.course

import org.ionproject.core.common.Media
import org.ionproject.core.common.RequiresAuthentication
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
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
class CourseController(private val courseServices: CourseServices) {

    @GetMapping(Uri.courses, produces = [Media.SIREN_TYPE])
    fun getCourses(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val courses = courseServices.getCourses(page, limit)
        val siren = courses.courseToListRepr(page, limit)

        return ResponseEntity.ok(siren)
    }

    @GetMapping(Uri.courseById, produces = [Media.SIREN_TYPE])
    fun getCourse(@PathVariable cid: Int): ResponseEntity<Siren> {
        val course = courseServices.getCourseById(cid)

        course?.let { return ResponseEntity.ok(it.courseToDetailRepr()) }
        return ResponseEntity.notFound().build()
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