package org.ionproject.core.readApi.course


import org.ionproject.core.readApi.common.Media
import org.ionproject.core.readApi.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class CourseErrorTest : ControllerTester() {

    companion object {
        val notFoundUri = Uri.forCourseById(-1)
        val validUri = Uri.forCourseById(1)

        val badRequestUri = URI("/v0/courses/a")

    }

    /**
     * Not found course with
     * id = -1
     */
    @Test
    fun getInvalidCourse() {
        doGet(notFoundUri) {
            header("Authorization", readTokenTest)
        }
                .andDo { print() }
                .andExpect {
                    status {isNotFound}
                }.andReturn()
    }

    /**
     * Requesting course with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getCourseWithInvalidContentType() {
        doGet(validUri) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }
                .andDo { print() }
                .andExpect { status {isNotAcceptable} }
                .andReturn()
    }

    /**
     * Bad request with letters instead of a course id
     */
    @Test
    fun getCourseWithBadRequest() {
        doGet(badRequestUri) {
            header("Authorization", readTokenTest)
        }
                .andDo { print() }
                .andExpect { status {isBadRequest} }
                .andReturn()
    }


}