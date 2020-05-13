package org.ionproject.core.calendar

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.properties.MultiValuedProperty
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.common.Media
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import java.io.PrintWriter
import java.io.Writer
import java.lang.reflect.Type

class ICalendarHttpMessageConverter : AbstractGenericHttpMessageConverter<Calendar>(Media.MEDIA_TEXT_CALENDAR) {


  override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean = false

  override fun supports(clazz: Class<*>): Boolean = clazz == Calendar::class.java

  override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Calendar =
    throw UnsupportedOperationException("This message converter can't read.")

  override fun writeInternal(t: Calendar, type: Type?, outputMessage: HttpOutputMessage) {
    PrintWriter(outputMessage.body).apply {
      writeCalendar(t, this)
      close()
    }
  }

  override fun readInternal(clazz: Class<out Calendar>, inputMessage: HttpInputMessage): Calendar =
    throw UnsupportedOperationException("This message converter can't read.")

  private fun writeCalendar(calendar: Calendar, writer: Writer) {
    writer.writeICalendar("BEGIN:VCALENDAR")

    calendar.apply {
      writeProperty(prod, writer)
      writeProperty(version, writer)
      if (scale != null) {
        writeProperty(scale, writer)
      }

      if (method != null) {
        writeProperty(method, writer)
      }

      forEach {
        writeComponent(it, writer)
      }
    }

    writer.writeICalendar("END:VCALENDAR")
  }

  private fun writeComponent(calendarComponent: CalendarComponent, writer: Writer) {
    calendarComponent.apply {
      writer.writeICalendar("BEGIN:$componentName")

      properties.forEach {
        writeProperty(it, writer)
      }

      writer.writeICalendar("END:$componentName")
    }
  }

  private fun writeProperty(property: Property, writer: Writer) {
    property.apply {
      val parameters = if (this is ParameterizedProperty) {
        parameters.map { ";${it.name}=${it.values.joinToString(",")}" }
          .let { if (it.isNotEmpty()) it.reduce(String::plus) else "" }
      } else ""

      val value = if (this is MultiValuedProperty<*>) {
        value.joinToString(",")
      } else value.toString()

      writer.writeICalendar("$name$parameters:$value")
    }
  }
}

private fun Writer.writeln(obj: Any) {
  write(obj.toString() + "\r\n")
}

private fun Writer.writeICalendar(string: String) {
  writeln(string.iCalendarFold())
}