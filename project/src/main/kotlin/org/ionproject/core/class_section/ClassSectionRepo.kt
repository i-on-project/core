package org.ionproject.core.class_section

import org.springframework.stereotype.Component

@Component
class ClassSectionRepo {
    fun get(acr: String, calendarTerm: String, id: String): ClassSection =
        ClassSection(acr, calendarTerm, id) // for now
}