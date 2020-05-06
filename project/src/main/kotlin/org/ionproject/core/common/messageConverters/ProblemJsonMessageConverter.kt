package org.ionproject.core.common.messageConverters

import org.ionproject.core.common.Media
import org.ionproject.core.common.ProblemJson
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.io.PrintWriter
import java.lang.reflect.Type

class ProblemJsonMessageConverter(private val converter: MappingJackson2HttpMessageConverter) :
    AbstractGenericHttpMessageConverter<ProblemJson>(Media.MEDIA_ALL) {
    override fun supports(clazz: Class<*>): Boolean = clazz == ProblemJson::class.java

    override fun writeInternal(t: ProblemJson, type: Type?, outputMessage: HttpOutputMessage) {
        PrintWriter(outputMessage.body).apply {
            converter.write(t, Media.MEDIA_PROBLEM, outputMessage)
            close()
        }
    }

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): ProblemJson =
        throw UnsupportedOperationException("This converter can't read.")

    override fun readInternal(clazz: Class<out ProblemJson>, inputMessage: HttpInputMessage): ProblemJson =
        throw UnsupportedOperationException("This converter can't read.")

}