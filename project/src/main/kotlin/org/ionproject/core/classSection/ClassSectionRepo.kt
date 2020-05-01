package org.ionproject.core.classSection

interface ClassSectionRepo {
    fun get(cid: Int, calendarTerm: String, id: String): ClassSection?
}
