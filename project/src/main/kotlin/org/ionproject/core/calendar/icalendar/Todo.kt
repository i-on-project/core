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
    summary: Summary,
    description: Description,
    attachment: Attachment?,
    dateTimeStamp: DateTimeStamp,
    created: DateTimeCreated,
    due: DateTimeDue,
    categories: Categories
) : CalendarComponent(uniqueIdentifier, summary, description, attachment, dateTimeStamp, created, due, categories) {
    override val componentName: String
        get() = iCalName

    companion object {
        private const val iCalName = "VTODO"
    }
}