package org.ionproject.core.calendar

/**
 * DB Mock for calendar data
 */
class CalendarData {

/*
Get all events of a calendar having the calendar id
SELECT cmp.id, cmp.type, cmp.dtstamp, cmp.summary, cmp.description, cmp.dtstart, cmp.dtend, cc.category, cc.language, rec.frequency, rec.by_day, rec.interval, rec.count
FROM CalendarComponent AS cmp
LEFT JOIN RecurrenceRule AS rec ON rec.event_id = cmp.id
LEFT JOIN ComponentCategories AS cc ON cmp.id = cc.event_id
WHERE cmp.calendar_id = {calendar id}
*/

}