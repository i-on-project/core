package org.ionproject.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

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

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}
