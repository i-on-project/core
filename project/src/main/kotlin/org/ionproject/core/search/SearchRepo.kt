package org.ionproject.core.search

import org.ionproject.core.calendarTerm.sql.CalendarTermData
import org.ionproject.core.classSection.sql.ClassSectionData
import org.ionproject.core.course.sql.CourseData
import org.ionproject.core.klass.sql.KlassData
import org.ionproject.core.programme.sql.ProgrammeData
import org.ionproject.core.search.model.InvalidSearchTypeException
import org.ionproject.core.search.model.SearchQuery
import org.ionproject.core.search.model.SearchResultCollection
import java.lang.IllegalArgumentException

object SearchableEntities {
    const val PROGRAMME = "programme"
    const val COURSE = "course"
    const val CLASS = "class"
    const val CLASS_SECTION = "class-section"
    const val CALENDAR_TERM = "calendar-term"

    val ALL = listOf(PROGRAMME, COURSE, CLASS, CLASS_SECTION, CALENDAR_TERM)
}


enum class SearchableEntity(
    private val alternateName: String
) {
    PROGRAMME(SearchableEntities.PROGRAMME),
    COURSE(SearchableEntities.COURSE),
    CLASS(SearchableEntities.CLASS),
    CLASS_SECTION(SearchableEntities.CLASS_SECTION),
    CALENDAR_TERM(SearchableEntities.CALENDAR_TERM);

    override fun toString(): String = alternateName

    companion object {
        const val ALL = "${SearchableEntities.PROGRAMME},${SearchableEntities.COURSE},${SearchableEntities.CLASS},${SearchableEntities.CLASS_SECTION},${SearchableEntities.CALENDAR_TERM}"
        val QUERY_MAP = mapOf(
            PROGRAMME to ProgrammeData.SEARCH_PROGRAMMES,
            COURSE to CourseData.SEARCH_COURSES,
            CLASS to KlassData.SEARCH_CLASSES,
            CLASS_SECTION to ClassSectionData.SEARCH_CLASS_SECTIONS,
            CALENDAR_TERM to CalendarTermData.SEARCH_CALENDAR_TERMS
        )
        fun parse(name: String): SearchableEntity = values().find { it.alternateName == name } ?: throw InvalidSearchTypeException(name)
    }
}

interface SearchRepo {
    fun search(query: SearchQuery) : SearchResultCollection
}