package org.ionproject.core.programme

import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.ionproject.core.utils.ControllerTester
import org.ionproject.core.utils.readTokenTest
import org.junit.jupiter.api.Test
import java.net.URI

internal class ProgrammeErrorTest : ControllerTester() {

    companion object {
        val notFoundUri = Uri.forProgrammesById(-1)
        val validUri = Uri.forProgrammesById(1)
        val notFoundProgrammeOffer = Uri.forProgrammeOfferById(1, -1)
        val badRequestProgrammeOffer = URI("/api/programmes/1/offers/a")
        val badRequestUri = URI("/api/programmes/a") // Can't use forProgrammesById as won't accept a bad value
    }

    /**
     * Not found programme with
     * id = -1
     */
    @Test
    fun getInvalidProgramme() {
        doGet(notFoundUri) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isNotFound() }
            }.andReturn()
    }

    /**
     * Requesting programme with a content-type "JSON-HOME"
     * that is not supported.
     */
    @Test
    fun getProgrammeWithInvalidContentType() {
        doGet(validUri) {
            accept = Media.MEDIA_HOME
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect { status { isNotAcceptable() } }
            .andReturn()
    }

    /**
     * Bad request with letters instead of a programme id
     */
    @Test
    fun getProgrammeWithBadRequest() {
        doGet(badRequestUri) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect { status { isBadRequest() } }
            .andReturn()
    }

    /**
     * Programme Offer Test
     */
    @Test
    fun getInvalidProgrammeOffer() {
        doGet(notFoundProgrammeOffer) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect {
                status { isNotFound() }
            }.andReturn()
    }

    /**
     * Bad request programme offer
     */
    @Test
    fun getProgrammeOfferWithBadRequest() {
        doGet(badRequestProgrammeOffer) {
            header("Authorization", readTokenTest)
        }.andDo { println() }
            .andExpect { status { isBadRequest() } }
            .andReturn()
    }
}
