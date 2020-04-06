package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeCreated
import org.ionproject.core.calendar.icalendar.properties.components.change_management.DateTimeStamp
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeEnd
import org.ionproject.core.calendar.icalendar.properties.components.datetime.DateTimeStart
import org.ionproject.core.calendar.icalendar.properties.components.datetime.Duration
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Categories
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Description
import org.ionproject.core.calendar.icalendar.properties.components.descriptive.Summary
import org.ionproject.core.calendar.icalendar.properties.components.recurrence.RecurrenceRule
import org.ionproject.core.calendar.icalendar.properties.components.relationship.UniqueIdentifier

class Event private constructor(
    uid: UniqueIdentifier,
    summary: Summary,
    description: Description,
    stamp: DateTimeStamp,
    created: DateTimeCreated,
    categories: Categories,
    start: DateTimeStart,
    end: DateTimeEnd?,
    duration: Duration?,
    recurrenceRule: RecurrenceRule?
) : CalendarComponent(uid, summary, description, stamp, created, categories, start, end, duration, recurrenceRule) {

    constructor(
        uid: UniqueIdentifier,
        summary: Summary,
        description: Description,
        stamp: DateTimeStamp,
        created: DateTimeCreated,
        categories: Categories,
        start: DateTimeStart,
        duration: Duration,
        recurrenceRule: RecurrenceRule? = null
    ) : this(uid, summary, description, stamp, created, categories, start, null, duration, recurrenceRule)

    constructor(
        uid: UniqueIdentifier,
        summary: Summary,
        description: Description,
        stamp: DateTimeStamp,
        created: DateTimeCreated,
        categories: Categories,
        start: DateTimeStart,
        end: DateTimeEnd,
        recurrenceRule: RecurrenceRule? = null
    ) : this(uid, summary, description, stamp, created, categories, start, end, null, recurrenceRule)

    override val componentName: String
        get() = iCalName

    companion object {
        private const val iCalName = "VEVENT"
    }
}