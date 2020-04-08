package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.common.TEXT_CALENDAR_MEDIA_TYPE
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import java.io.PrintWriter
import java.io.Writer
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

class ICalendarHttpMessageConverter : AbstractGenericHttpMessageConverter<Calendar>(TEXT_CALENDAR_MEDIA_TYPE) {

    override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean = false

    override fun supports(clazz: Class<*>): Boolean = clazz == Calendar::class.java

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Calendar = throw UnsupportedOperationException("This message converter can't read.")

    override fun writeInternal(t: Calendar, type: Type?, outputMessage: HttpOutputMessage) {
        when(outputMessage.headers.contentType) {
            null -> {
                outputMessage.headers.contentType = TEXT_CALENDAR_MEDIA_TYPE
                PrintWriter(outputMessage.body).apply {
                    toiCalendar(t, this)
                    close()
                }
            }
            TEXT_CALENDAR_MEDIA_TYPE -> PrintWriter(outputMessage.body).apply {
                toiCalendar(t, this)
                close()
            }
        }
    }

    override fun readInternal(clazz: Class<out Calendar>, inputMessage: HttpInputMessage): Calendar = throw UnsupportedOperationException("This message converter can't read.")

    private fun toiCalendar(calendar: Calendar, output: Writer) {
        output.writeICalendar("BEGIN:VCALENDAR")

        calendar.apply {
            toiCalendar(prod, output)
            toiCalendar(version, output)
            if (scale != null) {
                toiCalendar(scale, output)
            }

            if (method != null) {
                toiCalendar(method, output)
            }

            forEach {
                toiCalendar(it, output)
            }
        }

        output.writeICalendar("END:VCALENDAR")
    }

    private fun toiCalendar(calendarComponent: CalendarComponent, output: Writer) {
        calendarComponent.apply {
            output.writeICalendar("BEGIN:$componentName")

            properties.forEach {
                toiCalendar(it, output)
            }

            output.writeICalendar("END:$componentName")
        }
    }

    private fun toiCalendar(property: Property, output: Writer) {
        property.apply {
            val parameters = if (this is ParameterizedProperty) {
                parameters.map { ";${it.name}=${it.values.joinToString(",")}" }.let { if (it.isNotEmpty()) it.reduce(String::plus) else "" }
            } else ""

            val value = if (this is MultiValuedProperty<*>) {
                value.joinToString(",")
            } else value.toString()

            output.writeICalendar("$name$parameters:$value")
        }
    }
}