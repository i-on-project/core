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

    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "ATTACH"

        operator fun invoke(uri: Uri?, binary: Binary?, formatType: FormatType?) : Attachment {
            if (uri != null) {
                if (binary != null) throw IllegalArgumentException("Can't specify both uri and binary.")
                return Attachment(uri, null, null, null)
            }
            if (binary == null) throw IllegalArgumentException("Need to specify both uri or binary. Both can not be null.")
            return Attachment(
                binary,
                InlineEncoding(InlineEncoding.Type.BASE64),
                ValueDataType(binary),
                formatType
            )
        }
    }
}