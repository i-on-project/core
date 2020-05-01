package org.ionproject.core.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.web.util.UriTemplate
import java.net.URI

fun URI.toTemplate(): UriTemplate = UriTemplate(this.toString())

class UriTemplateSerializer : StdSerializer<UriTemplate>(UriTemplate::class.java) {

    override fun serialize(value: UriTemplate?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value == null || gen == null) {
            return
        }
        gen.writeString(value.toString())
    }

}
