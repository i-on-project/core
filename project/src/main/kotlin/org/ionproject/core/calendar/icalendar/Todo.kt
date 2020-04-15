package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeDue
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier

class Todo(
    uniqueIdentifier: UniqueIdentifier,
    val summary: Summary,
    val description: Description,
    val attachment: Attachment?,
    val dateTimeStamp: DateTimeStamp,
    val created: DateTimeCreated,
    val due: DateTimeDue,
    val categories: Categories
) : CalendarComponent(uniqueIdentifier, dateTimeStamp, summary, description, attachment, created, due, categories) {
    override val componentName: String
        get() = iCalName

    companion object {
        private const val iCalName = "VTODO"
    }
}