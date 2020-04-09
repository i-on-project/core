package org.ionproject.core.klass

import org.ionproject.core.ControllerTester
import org.ionproject.core.common.APPLICATION_TYPE
import org.ionproject.core.common.SIREN_MEDIA_TYPE
import org.ionproject.core.common.SIREN_SUBTYPE
import org.ionproject.core.common.Uri
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import java.net.URI

internal class KlassControllerTest : ControllerTester() {

    fun isValidSiren(uri: URI) = mocker.get(uri) {
        accept = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
    }.andExpect {
        status { isOk }
        content { contentType(SIREN_MEDIA_TYPE) }
        jsonPath("$.links") { exists() }
        jsonPath("$.actions") { exists() }
    }.andReturn()

    @Test
    fun getClassResource_shouldRespondWithTheSirenRepresentationOfClass() {
        isValidSiren(Uri.forKlassByTerm(1, "1920v"))
    }

    @Test
    fun getClassCollectionResource_shouldRespondWithTheSirenRepresentationOfClassCollection() {
        isValidSiren(Uri.forKlasses(1))
    }

    @Test
    fun getPage_shouldRespondWithTheCorrectNumberOfItems() {
        var size = 1
        mocker.get("${Uri.forKlasses(1)}?size=$size") {
            accept = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
        }.andExpect {
            status { isOk }
            content { contentType(SIREN_MEDIA_TYPE) }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(size) }
        }

        size = 2
        mocker.get("${Uri.forKlasses(1)}?size=$size") {
            accept = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE)
        }.andExpect {
            status { isOk }
            content { contentType(SIREN_MEDIA_TYPE) }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(size) }
        }
    }

}