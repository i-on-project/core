package org.ionproject.core

import org.ionproject.core.calendar.ICalendarHttpMessageConverter
import org.ionproject.core.calendar.icalendar.Calendar
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {
	override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
		val a = MappingJackson2HttpMessageConverter()
		converters.add(ICalendarHttpMessageConverter())
	}
}

@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}
