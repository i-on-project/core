package org.ionproject.core.klass

import org.ionproject.core.classSection.ClassSection
import org.springframework.stereotype.Component

@Component
class KlassRepo {
    fun get(acr: String, calendarTerm: String): FullKlass =
        FullKlass(
                acr,
                calendarTerm,
                listOf(ClassSection(acr, calendarTerm, "1d"), ClassSection(acr, calendarTerm, "2d")))

    fun getPage(acr: String, page: Int, size: Int): List<Klass> =
        listOf(
            Klass(acr, "1920v"),
            Klass(acr, "1920i"),
            Klass(acr, "1819v"))
}