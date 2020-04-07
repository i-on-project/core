package org.ionproject.core.classSection

import org.springframework.stereotype.Component

@Component
class ClassSectionRepo {
    fun get(acr: String, calendarTerm: String, id: String): ClassSection =
        ClassSection(acr, calendarTerm, id)
}