package org.ionproject.core.readApi.calendar.icalendar.properties.components.recurrence

import org.ionproject.core.readApi.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.TimeZoneIdentifier
import org.ionproject.core.readApi.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.readApi.calendar.icalendar.types.Date
import org.ionproject.core.readApi.calendar.icalendar.types.DateTime
import org.ionproject.core.readApi.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.readApi.calendar.icalendar.types.MultiValue

class ExceptionDateTime(
    vararg values: ICalendarDataType,
    val timeZoneIdentifier: TimeZoneIdentifier? = null
) : MultiValuedProperty<ICalendarDataType>, ParameterizedProperty {

    val valueDataType: ValueDataType?

    init {
        val type: ICalendarDataType?
        if (values.isNotEmpty()) {
            type = values[0]
            val klass = type::class.java

            if (klass != Date::class.java && klass != DateTime::class.java) throw IllegalArgumentException("The values must be Date or DateTime.")

            if (values.any { it::class.java != klass }) throw IllegalArgumentException("The values must be all of the same value type.")

            valueDataType = if (klass == Date::class.java) ValueDataType(type)
            else null // if it is not Date we can leave null and use the default which is DateTime
        } else {
            valueDataType = null
        }
    }

    override val parameters: List<PropertyParameter>
        get() = listOfNotNull(valueDataType, timeZoneIdentifier)

    override val name: String
        get() = "EXDATE"

    override val value: MultiValue<ICalendarDataType> = MultiValue(*values)
}