package org.ionproject.core.readApi.course

import org.ionproject.core.readApi.common.Siren
import org.ionproject.core.readApi.common.Uri
import org.ionproject.core.readApi.course.representations.courseToDetailRepr
import org.ionproject.core.readApi.course.representations.courseToListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CourseController(private val courseServices: CourseServices) {

    @GetMapping(Uri.courses)
    fun getCourses(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Siren> {
        val courses = courseServices.getCourses(page, limit)
        val siren = courses.courseToListRepr(page, limit)

        return ResponseEntity.ok(siren)
    }

    @GetMapping(Uri.courseById)
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
    fun deleteCourse(@PathVariable id: Int) {
        TODO("Waiting write API")
    }

    @PatchMapping(Uri.courseById)
    fun editCourse(@PathVariable id: Int) {
        TODO("Waiting write API")
    }
}