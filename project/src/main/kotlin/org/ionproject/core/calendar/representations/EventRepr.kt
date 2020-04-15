package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Event
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.ClassSection



fun eventRepr(cs: ClassSection, ev : Event) =
        SirenBuilder(PropertiesEventRepr(ev.uid,ev.summary,ev.description,ev.dtStamp,ev.created,ev.categories,ev.start,ev.end))
                .klass("event")
                .entities(listOf(buildSubEntity(cs)))
                .link("self", Uri.forComponentByCalendar(cs.courseId,cs.calendarTerm,ev.uid.value.toString()))
                .link("about", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
                .toSiren()


private fun buildSubEntity(cs: ClassSection) =
        SirenBuilder(EventPropertiesClassSectionRepr(cs.id,cs.calendarTerm))
                .klass("class")
                .rel(Uri.REL_CLASS)
                .link("self", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
                .link("term", Uri.forTermByCal(cs.calendarTerm))
                .link("course",Uri.forCourseById(cs.courseId))
                .toEmbed()


/*The READ API representation doesn't include all properties of event
* (recurrence rule and duration)
*/
data class PropertiesEventRepr(val uid: UniqueIdentifier,
                               val summary: Summary,
                               val description: Description,
                               val stamp: DateTimeStamp,
                               val created: DateTimeCreated,
                               val categories: Categories,
                               val start: DateTimeStart,
                               val end: DateTimeEnd?)

private data class EventPropertiesClassSectionRepr(val classId: String, val termId: String)