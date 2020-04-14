package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.properties.calendar.ProductIdentifier
import org.ionproject.core.calendar.icalendar.properties.calendar.Version
import org.ionproject.core.common.*
import org.ionproject.core.common.model.ClassSection
import org.springframework.http.HttpMethod
import java.net.URI

fun calendarRepr(classSection: ClassSection, calendar: Calendar) =
    SirenBuilder(PropertiesCalendarRepr("calendar", calendar.prod, calendar.version, calendar.components))
        .entities(listOf(buildSubEntity(classSection)))
        .action(
            Action(
                name = "search",
                title = "Search components",
                method = HttpMethod.GET,
                href = URI("${Uri.calendarByClass}?type,startBefore,startAfter,endBefore,endAfter,summary"),
                isTemplated = true,
                type = Media.APPLICATION_JSON,
                fields = listOf(
                    Field(name = "type", type = "text", klass = "https://example.org/param/free-text-query"),
                    Field(name = "startBefore", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "startAfter", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "endBefore", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "endAfter", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "summary", type = "text", klass = "https://example.org/param/free-text-query")
                )
            )
        )
        .action(
            Action(
                name = "add-item",
                title = "Add  Item",
                method = HttpMethod.POST,
                href = Uri.forCalendarByClass(classSection.courseId, classSection.id),
                isTemplated = false,
                type = Media.APPLICATION_JSON,
                fields = listOf()
            )
        ).action(
            Action(
                name = "batch-delete",
                title = "Delete multiple items",
                method = HttpMethod.DELETE,
                isTemplated = true,
                href = Uri.forCalendarByClass(classSection.courseId, classSection.id),
                fields = listOf(
                    Field(name = "type", type = "text", klass = "https://example.org/param/free-text-query")
                )
            )
        )
        .link("self", Uri.forClassSectionById(classSection.courseId, classSection.id, classSection.id))
        .link("about", Uri.forClassSectionById(classSection.courseId, classSection.id, classSection.id))

fun buildSubEntity(classSection: ClassSection) =
    SirenBuilder(PropertiesClassSectionRepr(classSection.id, classSection.lecturer))
        .klass("class", "section")
        .rel(Uri.REL_CLASS_SECTION)
        .link("self", Uri.forClassSectionById(classSection.courseId, classSection.calendarTerm, classSection.id))
        .toEmbed()


data class PropertiesClassSectionRepr(val uid: String, val lecturer: Int)

data class PropertiesCalendarRepr(val type: String,
                                  val prodid: ProductIdentifier,
                                  val version: Version,
                                  val subComponents: MutableList<CalendarComponent>)