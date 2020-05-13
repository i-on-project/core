package org.ionproject.core.calendar.representations

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.ionproject.core.calendar.icalendar.Calendar

class CalendarSerializer(klass: Class<Calendar>? = null) : StdSerializer<Calendar>(klass) {

  override fun serialize(value: Calendar, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeObject(value.toSiren())
  }
}