package org.ionproject.core.classSection

import org.ionproject.core.ControllerTester
import org.ionproject.core.common.Uri
import org.junit.jupiter.api.Test

internal class ClassSectionControllerTest : ControllerTester() {

    @Test
    fun getClassSection_shouldRespondWithTheSirenRepresentationOfClassSection() {
        isValidSiren(Uri.forClassSectionById(1, "1920v", "1D"))
    }
}