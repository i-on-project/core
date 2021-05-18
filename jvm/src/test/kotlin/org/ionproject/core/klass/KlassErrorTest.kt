package org.ionproject.core.klass

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class KlassErrorTest : ControllerTester() {

    companion object {
        val notFoundUriByCourseId = Uri.forKlasses(-1)
        val validUriByCourseId = Uri.forKlasses(1)
        val notFoundUriByCalTerm = Uri.forKlassByCalTerm(1, "xijsx")
        val notFoundUriByCalTermWithBadCID = Uri.forKlassByCalTerm(-1, "1920i")
        val validUriByCalTerm = Uri.forKlassByCalTerm(1, "1920i")
        val badRequestUri = URI("/api/courses/a/classes")
        val badRequestUriCalTerm = URI("/api/courses/a/classes/1920i/")
    }

    /**
     * Not found class with courseId=-1
     */
    @Test
    fun getInvalidKlass() {
        doGet(notFoundUriByCourseId) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }

    /**
     * Requesting programme with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getKlassWithInvalidContentType() {
        doGet(validUriByCourseId) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isNotAcceptable } }
            .andReturn()
    }

    /**
     * Bad request with letters instead of a class id
     */
    @Test
    fun getKlassWithBadRequest() {
        doGet(badRequestUri) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isBadRequest } }
            .andReturn()
    }

    /**
     * Not found by calTerm
     */
    @Test
    fun getInvalidKlassByCalTerm() {
        doGet(notFoundUriByCalTerm) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }

    /**
     * Requesting with unsupported content-type
     */
    @Test
    fun getKlassCalTermWithInvalidContentType() {
        doGet(validUriByCalTerm) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isNotAcceptable } }
            .andReturn()
    }

    /**
     * Bad request of class with calTerm
     */
    @Test
    fun getKlassCalTermBadRequest() {
        doGet(badRequestUriCalTerm) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect { status { isBadRequest } }
            .andReturn()
    }

    /**
     * Not found class with courseId=-1
     */
    @Test
    fun getInvalidKlassWithCalTermBadCID() {
        doGet(notFoundUriByCalTermWithBadCID) {
            header("Authorization", readTokenTest)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound }
            }.andReturn()
    }
}
