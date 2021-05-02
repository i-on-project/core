package org.ionproject.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import org.ionproject.core.accessControl.AccessControlCache
import org.ionproject.core.accessControl.PDP
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.calendar.ICalendarHttpMessageConverter
import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.representations.CalendarSerializer
import org.ionproject.core.common.Media
import org.ionproject.core.common.UriTemplateSerializer
import org.ionproject.core.common.argumentResolvers.PaginationResolver
import org.ionproject.core.common.customExceptions.EnvironmentVariableNotFoundException
import org.ionproject.core.common.interceptors.ControlAccessInterceptor
import org.ionproject.core.common.messageConverters.JsonHomeMessageConverter
import org.ionproject.core.common.messageConverters.ProblemJsonMessageConverter
import org.ionproject.core.common.messageConverters.SirenMessageConverter
import org.ionproject.core.user.auth.AuthMethod
import org.ionproject.core.user.auth.AuthMethodRegistry
import org.ionproject.core.user.auth.AuthNotificationRegistry
import org.ionproject.core.user.auth.EmailAuthMethod
import org.ionproject.core.user.common.email.EmailService
import org.ionproject.core.user.common.email.SendGridEmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.util.UriTemplate

private const val SENDGRID_API_KEY = "SENDGRID_API_KEY"
private const val EMAIL_SENDER_EMAIL = "EMAIL_SENDER_EMAIL"
private const val EMAIL_SENDER_NAME = "EMAIL_SENDER_NAME"

@SpringBootApplication
class CoreApplication

@Configuration
@EnableWebMvc
class CoreSerializationConfig : WebMvcConfigurer {
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val converter =
            converters.find { it is MappingJackson2HttpMessageConverter } as MappingJackson2HttpMessageConverter

        // Additional options for Jackson
        converter.objectMapper // default used by spring
            .setSerializationInclusion(JsonInclude.Include.NON_NULL) // ignore null properties
            .configure(SerializationFeature.INDENT_OUTPUT, true) // json pretty output
            .registerModule(
                SimpleModule()
                    .addSerializer(UriTemplate::class.java, UriTemplateSerializer())
                    .addSerializer(Calendar::class.java, CalendarSerializer())
            )

        converter.supportedMediaTypes = listOf(Media.MEDIA_JSON)

        // Registering Message Converters
        converters.add(0, SirenMessageConverter(converter))
        converters.add(0, JsonHomeMessageConverter(converter))
        converters.add(0, ProblemJsonMessageConverter(converter))
        converters.add(0, ICalendarHttpMessageConverter()) // Calendar -> text/calendar || application/vdn.siren+json

        /**
         * Converters were added to position 0 to be more privileged than the default
         * message converters. (Converters follow LDFC order, last added first checked)
         * (If such wasn't done, the default behavior (without specifying the Accept Header) would always be JSON)
         *
         * Also, if the property supportedMediaTypes of MappingJackson2HttpMessageConverter wasn't overriden
         * a controller which returns hypermedia "JSON-HOME" would accept the header "Accept: application/vnd.siren+json"
         * and return a document of type "JSON-HOME" with the header "Content-Type:application/vnd.siren+json",
         * because the default message converter matches everything with "application/json" and "application/*+json}".*/
         *
         */
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(controlAccessInterceptor())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(PaginationResolver())
    }

    @Autowired
    lateinit var pdp: PDP

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Autowired
    lateinit var cache: AccessControlCache

    @Bean
    fun controlAccessInterceptor(): ControlAccessInterceptor {
        return ControlAccessInterceptor(pdp, tokenGenerator, cache)
    }

    @Bean
    fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Bean
    fun buildEmailService(@Autowired httpClient: OkHttpClient): EmailService {
        val objectMapper = jacksonObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val apiKey = System.getenv(SENDGRID_API_KEY) ?: ""
        val senderEmail = System.getenv(EMAIL_SENDER_EMAIL) ?: ""
        val senderName = System.getenv(EMAIL_SENDER_NAME) ?: ""

        return SendGridEmailService(
            httpClient,
            objectMapper,
            apiKey,
            senderEmail,
            senderName
        )
    }

    @Bean
    fun getAuthMethodRegistry(@Autowired emailService: EmailService): AuthMethodRegistry {
        val registry = AuthMethodRegistry()

        registry.register(EmailAuthMethod(
            listOf("*.isel.pt", "*.isel.ipl.pt"),
            emailService
        ))

        return registry
    }

    @Bean
    fun getAuthNotificationRegistry(): AuthNotificationRegistry {
        val notificationRegistry = AuthNotificationRegistry()
        notificationRegistry.register("POLL")
        return notificationRegistry
    }
}

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
