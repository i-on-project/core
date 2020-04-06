package org.ionproject.core.calendar.icalendar.properties.components.descriptive

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.FormatType
import org.ionproject.core.calendar.icalendar.properties.parameters.InlineEncoding
import org.ionproject.core.calendar.icalendar.properties.parameters.ValueDataType
import org.ionproject.core.calendar.icalendar.types.Binary
import org.ionproject.core.calendar.icalendar.types.Uri

class Attachment private constructor(
    value: Any,
    inlineEncoding: InlineEncoding?,
    valueDataType: ValueDataType?,
    formatType: FormatType?
) : Property(value, inlineEncoding, valueDataType, formatType) {

    constructor(uri: Uri) : this(uri, null, null, null)

    constructor(binary: Binary, formatType: FormatType? = null) : this(binary,
    InlineEncoding(InlineEncoding.Type.BASE64),
    ValueDataType(binary),
    formatType)

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "ATTACH"
    }
}