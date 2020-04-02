package org.ionproject.core.calendar.icalendar.properties

import org.ionproject.core.calendar.icalendar.iCalendarVersion
import org.ionproject.core.calendar.icalendar.properties.params.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.DateTime
import org.ionproject.core.calendar.icalendar.types.DurationType
import org.ionproject.core.calendar.icalendar.types.Recur
import org.ionproject.core.calendar.icalendar.types.Text

abstract class Property(
    val name: String,
    val values: List<Any>,
    val parameters: List<PropertyParameter> = emptyList()
) {
    constructor(
        name: String,
        value: Any,
        parameters: List<PropertyParameter> = emptyList()
    ) : this(name, listOf(value), parameters)

    override fun toString(): String {
        val parameters = StringBuilder()
        this.parameters.forEach {
            parameters.append(";$it")
        }
        val values = this.values.map{
            it.toString()
        }.reduceRight { acc, str ->
            return "$str,$acc"
        }

        return "$name$parameters:$values"
    }
}
class ProdId(text: Text) : Property("PRODID", text)
class Version(text: Text = Text(iCalendarVersion)) : Property("VERSION", text)

class UID(value: Int) : Property("UID", value)

class DtStart(value: DateTime) : Property("DTSTART", value)
class DtStamp(value: DateTime) : Property("DTSTAMP", value)
class DtEnd(value: DateTime) : Property("DTEND", value)
class DtCreated(value: DateTime) : Property("CREATED", value)

class Summary(text: Text) : Property("SUMMARY", text)

class Description(text: Text) : Property("DESCRIPTION", text)

class Categories(categories: List<String>) : Property("CATEGORY", categories) {
    constructor(vararg categories: String) : this(categories.toList())
}

class Duration(duration: DurationType) : Property("DURATION", duration)

class RecurrenceRule(recur: Recur) : Property("RRULE", recur)

