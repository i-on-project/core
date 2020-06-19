package org.ionproject.core.readApi.common.messageConverters

import org.ionproject.core.readApi.common.Media
import org.ionproject.core.readApi.home.JsonHome
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.io.PrintWriter
import java.lang.reflect.Type

class JsonHomeMessageConverter(private val converter: MappingJackson2HttpMessageConverter) :
    AbstractGenericHttpMessageConverter<JsonHome>(Media.MEDIA_HOME) {
    override fun supports(clazz: Class<*>): Boolean = clazz == JsonHome::class.java

    override fun writeInternal(t: JsonHome, type: Type?, outputMessage: HttpOutputMessage) {
        PrintWriter(outputMessage.body).apply {
            converter.write(t, Media.MEDIA_HOME, outputMessage)
            close()
        }
    }

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): JsonHome =
        throw UnsupportedOperationException("This converter can't read.")

    override fun readInternal(clazz: Class<out JsonHome>, inputMessage: HttpInputMessage): JsonHome =
        throw UnsupportedOperationException("This converter can't read.")

}