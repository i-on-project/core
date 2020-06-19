package org.ionproject.core.readApi.common.messageConverters

import org.ionproject.core.readApi.calendar.icalendar.Calendar
import org.ionproject.core.readApi.common.Media
import org.ionproject.core.readApi.common.Siren
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.io.PrintWriter
import java.lang.reflect.Type

class SirenMessageConverter(private val converter: MappingJackson2HttpMessageConverter) :
    AbstractGenericHttpMessageConverter<Any>(Media.MEDIA_SIREN) {

    override fun supports(clazz: Class<*>): Boolean {
        return (clazz == Siren::class.java || clazz == Calendar::class.java)
    }

    override fun writeInternal(t: Any, type: Type?, outputMessage: HttpOutputMessage) {
        PrintWriter(outputMessage.body).apply {
            converter.write(t, Media.MEDIA_SIREN, outputMessage)
            close()
        }
    }

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Any =
        throw UnsupportedOperationException("This converter can't read.")

    override fun readInternal(clazz: Class<out Any>, inputMessage: HttpInputMessage): Any =
        throw UnsupportedOperationException("This converter can't read.")

}