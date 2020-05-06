package org.ionproject.core.common.messageConverters

import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.io.PrintWriter
import java.lang.reflect.Type

class SirenMessageConverter(private val converter: MappingJackson2HttpMessageConverter) :
    AbstractGenericHttpMessageConverter<Siren>(Media.MEDIA_SIREN) {

    override fun supports(clazz: Class<*>): Boolean {
        val result = clazz == Siren::class.java
        return result
    }

    override fun writeInternal(t: Siren, type: Type?, outputMessage: HttpOutputMessage) {
        PrintWriter(outputMessage.body).apply {
            converter.write(t, Media.MEDIA_SIREN, outputMessage)
            close()
        }
    }

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Siren =
        throw UnsupportedOperationException("This converter can't read.")

    override fun readInternal(clazz: Class<out Siren>, inputMessage: HttpInputMessage): Siren =
        throw UnsupportedOperationException("This converter can't read.")

}