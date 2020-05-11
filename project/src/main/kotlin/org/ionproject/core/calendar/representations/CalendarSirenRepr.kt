package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.common.*
import org.ionproject.core.mapEntries
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate
import java.net.URI

private const val CALENDAR_CLASS = "calendar"

fun Calendar.toSiren(): Siren =
    SirenBuilder(properties)
        .klass(CALENDAR_CLASS)
        .action(
            Action(
                name = "search",
                title = "Search components",
                method = HttpMethod.GET,
                href = UriTemplate("${prod.value}/calendar?type,startBefore,startAfter,endBefore,endAfter,summary"),
                isTemplated = true,
                type = Media.APPLICATION_JSON,
                fields = listOf(
                    Field(name = "type", type = "text", klass = "https://example.org/param/free-text-query"),
                    Field(name = "startBefore", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "startAfter", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "endBefore", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "endAfter", type = "date", klass = "https://example.org/param/date-query"),
                    Field(name = "summary", type = "text", klass = "https://example.org/param/free-text-query")
                )
            )
        )
        .action(
            Action(
                name = "add-item",
                title = "Add  Item",
                method = HttpMethod.POST,
                href = UriTemplate("${prod.value}/calendar"),
                isTemplated = false,
                type = Media.APPLICATION_JSON,
                fields = listOf()
            )
        )
        .action(
            Action(
                name = "batch-delete",
                title = "Delete multiple items",
                method = HttpMethod.DELETE,
                isTemplated = true,
                href = UriTemplate("${prod.value}/calendar"),
                fields = listOf(
                    Field(name = "type", type = "text", klass = "https://example.org/param/free-text-query")
                )
            )
        )
        .link("self", href = URI("${prod.value}/calendar"))
        .link("about", href = URI("${prod.value}"))
        .toSiren()


// Property names
private const val PROPERTIES = "properties"
private const val TYPE = "type"
private const val PRODID = "prodid"
private const val VERSION = "version"
private const val SUB_COMPONENTS = "subComponents"

private val Calendar.properties: Map<String, Any>
    get() = mapOf(
        TYPE to "calendar",
        PROPERTIES to mapOf(
            VERSION to version.toSiren(),
            PRODID to prod.toSiren()
        ),
        SUB_COMPONENTS to components.map(CalendarComponent::toSiren)
    )

private fun CalendarComponent.toSiren(): Map<String, Any> {
    val type = this::class.java.simpleName.toLowerCase()
    return mapOf(
        TYPE to type,
        PROPERTIES to properties.toMap()
    )
}

private fun Iterable<Property>.toMap(): Map<String, Any> =
    this
        .groupBy {
            it.name
        }
        .mapEntries {
            val propName = it.key.toLowerCase()
            val list = it.value
            if (list.size > 1) {
                propName to arrayOf(
                    list.map(Property::toSiren)
                )
            } else {
                propName to list[0].toSiren()
            }
        }

private fun Property.toSiren(): Any {
    val aux = this as? ParameterizedProperty

    val params = aux?.parameters?.associate {
        it.name.toLowerCase() to it.toSiren()
    }

    return if (params.isNullOrEmpty()) {
        object {
            val value = this@toSiren.value.value
        }
    } else {
        object {
            val parameters = params
            val value = this@toSiren.value.value
        }
    }
}

private fun PropertyParameter.toSiren(): Any {
    return if (values.isNotEmpty()) {
        if (values.size == 1) values[0]
        else values
    } else throw IllegalStateException("Instance of PropertyParameter with no values.")
}
