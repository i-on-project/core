package org.ionproject.core.readApi.classSection

interface ClassSectionRepo {
    fun get(cid: Int, calendarTerm: String, id: String): ClassSection?
}
