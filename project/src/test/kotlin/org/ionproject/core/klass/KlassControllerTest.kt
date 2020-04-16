package org.ionproject.core.klass

import org.ionproject.core.ControllerTester
import org.ionproject.core.common.Media
import org.ionproject.core.common.Uri
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class KlassControllerTest : ControllerTester() {
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
        var limit = 1
        mocker.get("${Uri.forKlasses(1)}?limit=$limit") {
            accept = Media.MEDIA_SIREN
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(limit) }
        }

        limit = 2
        mocker.get("${Uri.forKlasses(1)}?limit=$limit") {
            accept = Media.MEDIA_SIREN
        }.andExpect {
            status { isOk }
            content { contentType("application/vnd.siren+json") }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
            jsonPath("$.entities.length()") { value(limit) }
        }
    }

}