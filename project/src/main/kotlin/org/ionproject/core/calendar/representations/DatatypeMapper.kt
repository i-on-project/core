package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.common.Media
import org.springframework.http.MediaType

abstract class DatatypeMapper {

    companion object {
        fun forType(media: MediaType) : DatatypeMapper {
            return when(media) {
                Media.MEDIA_SIREN -> SirenDatatypeMapper()
                Media.MEDIA_TEXT_CALENDAR -> TextCalendarDataTypeMapper()
                else -> throw IllegalArgumentException("No datatype mappers for specified media type.")
            }
        }
    }

    abstract fun map(value: ICalendarDataType) : Any
}