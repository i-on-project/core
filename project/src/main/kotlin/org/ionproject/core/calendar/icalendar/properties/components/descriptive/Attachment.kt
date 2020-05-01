package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.FormatType
import org.ionproject.core.calendar.icalendar.properties.parameters.InlineEncoding
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Binary
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Uri

class Attachment private constructor(
    override val value: ICalendarDataType,
    val inlineEncoding: InlineEncoding?,
    val valueDataType: ValueDataType?,
    val formatType: FormatType?
) : ParameterizedProperty {

    constructor(uri: Uri) : this(uri, null, null, null)
    constructor(binary: Binary, formatType: FormatType? = null) : this(
        binary,
        InlineEncoding(InlineEncoding.Type.BASE64),
        ValueDataType(binary),
        formatType
    )

    override val parameters: List<PropertyParameter>
        get() = listOfNotNull(inlineEncoding, valueDataType, formatType)

    override val name: String
        get() = "ATTACH"
}