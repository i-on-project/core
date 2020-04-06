package org.ionproject.core.calendar.icalendar.properties.parameters

import org.ionproject.core.calendar.icalendar.types.ICalendarDataType

class ValueDataType(type: ICalendarDataType) : PropertyParameter("VALUE", type.name) {
    enum class Type(private val altName: String? = null) {
        BINARY,
        BOOLEAN,
        CAL_ADDRESS("CAL-ADDRESS"),
        DATE,
        DATE_TIME("DATE-TIME"),
        DURATION,
        FLOAT,
        INTEGER,
        PERIOD,
        RECUR,
        TEXT,
        TIME,
        URI,
        UTC_OFFSET("UTF-OFFSET");

        override fun toString(): String = altName ?: name
    }
}