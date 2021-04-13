package org.ionproject.core.course

import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.course.representations.courseToDetailRepr
import org.ionproject.core.course.representations.courseToListRepr
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CourseController(private val repo: CourseRepoImpl) {

    @ResourceIdentifierAnnotation(ResourceIds.GET_COURSES, ResourceIds.VERSION_0)
    @GetMapping(Uri.courses)
    fun getCourses(pagination: Pagination): ResponseEntity<Siren> {
        val courses = repo.getCourses(pagination.page, pagination.limit)
        val siren = courses.courseToListRepr(pagination.page, pagination.limit)
        return ResponseEntity.ok(siren)
    }

    @ResourceIdentifierAnnotation(ResourceIds.GET_COURSE, ResourceIds.VERSION_0)
    @GetMapping(Uri.courseById)
    fun getCourse(@PathVariable cid: Int): ResponseEntity<Siren> {
        val course = repo.getCourseById(cid)
        course?.let { return ResponseEntity.ok(it.courseToDetailRepr()) }
        return ResponseEntity.notFound().build()
    }
}
