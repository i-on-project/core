package org.ionproject.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import org.ionproject.core.calendar.ICalToTextCalHttpMessageConverter
import org.ionproject.core.common.Media
import org.ionproject.core.common.UriTemplateSerializer
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.interceptors.PageLimitQueryParamInterceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.util.UriTemplate

@SpringBootApplication
class CoreApplication

@Configuration
@EnableWebMvc
class CoreSerializationConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val converter = converters.find { it is MappingJackson2HttpMessageConverter } as MappingJackson2HttpMessageConverter

        // Additional options for Jackson
        converter.objectMapper // default used by spring
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)  // ignore null properties
            .configure(SerializationFeature.INDENT_OUTPUT, true) // json pretty output

        converter.objectMapper.registerModule(SimpleModule().addSerializer(UriTemplate::class.java, UriTemplateSerializer()))

        // JSON Home
        val homeConverter = MappingJackson2HttpMessageConverter()
        homeConverter.objectMapper = converter.objectMapper
        homeConverter.supportedMediaTypes = listOf(Media.MEDIA_HOME)
        converters.add(homeConverter)

        // Calendar -> text/calendar
        converters.add(ICalToTextCalHttpMessageConverter())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LoggerInterceptor())
        registry.addInterceptor(PageLimitQueryParamInterceptor())
                .addPathPatterns("/v?/calendar-terms*")
                .addPathPatterns("/v?/courses/**")
                .addPathPatterns("/v?/programmes*")
    }
}

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {

    }
}

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
