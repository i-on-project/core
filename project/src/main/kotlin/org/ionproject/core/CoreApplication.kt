package org.ionproject.core

import org.ionproject.core.calendar.ICalendarHttpMessageConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(ICalendarHttpMessageConverter())
    }
}

@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
