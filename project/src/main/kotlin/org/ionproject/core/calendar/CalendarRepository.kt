package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.*
import org.ionproject.core.calendar.icalendar.types.*

class CalendarRepository {

    operator fun get(id: Int): Calendar? = calendars[id]

    private val calendars = HashMap<Int, Calendar>()

    init {
        calendars.putAll(
            arrayOf(
                1 to Calendar(
                    ProdId(Text("/class/1")),
                    mutableListOf(
                        Event(
                            UID(1),
                            Summary(Text("Exame Daw")),
                            Description(Text("Exame de epoca normal de DAW")),
                            DtStamp(DateTime(Date.of(2020, 6, 4), Time.of(14, 0, 0))),
                            DtCreated(DateTime(Date.of(2020, 6, 4), Time.of(14, 0, 0))),
                            Categories("EXAM", "EVALUATION"),
                            DtStart(DateTime(Date.of(2020, 7, 4), Time.of(16, 30, 0))),
                            DtEnd(DateTime(Date.of(2020, 7, 4), Time.of(19, 0, 0))),
                            Duration(DurationWeek(2)),
                            RecurrenceRule(Recur(Frequency.WEEKLY))
                        )
                    )
                )
            )
        )
    }

}