package org.ionproject.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.ionproject.core.calendar.ICalendarHttpMessageConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class CoreApplication

@Configuration
class CoreSerializationConfig {
    @Bean
    fun objectMapper(): ObjectMapper =
        MappingJackson2HttpMessageConverter().objectMapper // default used by spring
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)  // ignore null properties
            .configure(SerializationFeature.INDENT_OUTPUT, true) // json pretty output
}

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(ICalendarHttpMessageConverter())
    }
}


fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
