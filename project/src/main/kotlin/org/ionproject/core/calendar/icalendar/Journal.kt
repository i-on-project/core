package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Attachment
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier

class Journal(
    uniqueIdentifier: UniqueIdentifier,
    val summary: Summary,
    val description: Description,
    val attachment: Attachment?,
    val dateTimeStamp: DateTimeStamp,
    val dateTimeStart: DateTimeStart,
    val created: DateTimeCreated,
    val categories: Categories
) : CalendarComponent(uniqueIdentifier, dateTimeStamp, summary, description, attachment, dateTimeStart, created, categories) {

    override val componentName: String
        get() = iCalName

    companion object {
        private const val iCalName = "VJOURNAL"
    }
}