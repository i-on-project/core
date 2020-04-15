package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Todo
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.ClassSection
import java.net.URI

fun todoRepr(cs: ClassSection, todo: Todo) =
    SirenBuilder(
        PropertiesTodoRepr(
            todo.uid, todo.summary,
            todo.description, todo.attachment, todo.dateTimeStamp,
            todo.created, todo.categories, todo.due
        )
    )
        .klass("todo").entities(listOf(buildSubEntity(cs)))
        .link("self", Uri.forComponentByCalendar(cs.courseId, cs.calendarTerm, todo.uid.value.toString()))
        .link("service-doc", URI("TODO"))
        .link("about", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
        .toSiren()


private fun buildSubEntity(cs: ClassSection) =
    SirenBuilder(TodoPropertiesClassSectionRepr(cs.id, cs.calendarTerm))
        .klass("class")
        .rel(Uri.REL_CLASS)
        .link("self", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
        .link("term", Uri.forTermByCal(cs.calendarTerm))
        .link("course", Uri.forCourseById(cs.courseId))
        .toEmbed()

data class PropertiesTodoRepr(
    val uid: UniqueIdentifier,
    val summary: Summary,
    val description: Description,
    val attachment: Attachment?,
    val stamp: DateTimeStamp,
    val created: DateTimeCreated,
    val categories: Categories,
    val end: DateTimeDue
)

private data class TodoPropertiesClassSectionRepr(val classId: String, val termId: String)