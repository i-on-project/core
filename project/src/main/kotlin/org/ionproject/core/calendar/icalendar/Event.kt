package org.ionproject.core.calendar.icalendar

import org.ionproject.core.calendar.icalendar.properties.*

class Event(
    uid: UID,
    summary: Summary,
    description: Description,
    stamp: DtStamp,
    created: DtCreated,
    categories: Categories,
    start: DtStart,
    end: DtEnd,
    duration: Duration,
    recurrenceRule: RecurrenceRule
) : CalendarComponent(
    listOf(
        uid,
        summary,
        description,
        stamp,
        created,
        categories,
        start,
        end,
        duration,
        recurrenceRule
    )
) {
    override val componentName: String = "EVENT"
}