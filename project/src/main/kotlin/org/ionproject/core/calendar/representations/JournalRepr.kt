package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Journal
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.RelatedTo
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.common.Uri
import org.ionproject.core.common.model.ClassSection
import java.net.URI

fun journalRepr(cs: ClassSection, jr: Journal) =
    SirenBuilder(
        PropertiesJournalRepr(
            jr.uid, jr.summary, jr.description,
            jr.categories, jr.created, jr.dateTimeStamp,
            jr.dateTimeStart, null
        )  //No object relatedTo
    )
        .klass("journal")
        .entities(listOf(buildSubEntity(cs)))
        .link("self", Uri.forComponentByCalendar(cs.courseId, cs.calendarTerm, jr.uid.value.toString()))
        .link("about", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
        .link("related", URI("TODO/RELATED/DOES/NOT/EXIST"))


private fun buildSubEntity(cs: ClassSection) =
    SirenBuilder(JournalPropertiesClassSectionRepr(cs.id, cs.calendarTerm))
        .klass("class")
        .rel(Uri.REL_CLASS)
        .link("self", Uri.forKlassByTerm(cs.courseId, cs.calendarTerm))
        .link("term", Uri.forTermByCal(cs.calendarTerm))
        .link("course", Uri.forCourseById(cs.courseId))
        .toEmbed()

private data class JournalPropertiesClassSectionRepr(val classId: String, val termId: String)

data class PropertiesJournalRepr(
    val uid: UniqueIdentifier,
    val summary: Summary,
    val description: Description,
    val categories: Categories,
    val created: DateTimeCreated,
    val stamp: DateTimeStamp,
    val start: DateTimeStart,
    val relatedTo: RelatedTo?
)