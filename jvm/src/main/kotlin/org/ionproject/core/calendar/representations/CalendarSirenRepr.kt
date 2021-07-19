package org.ionproject.core.calendar.representations

import org.ionproject.core.calendar.icalendar.Calendar
import org.ionproject.core.calendar.icalendar.CalendarComponent
import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.common.Action
import org.ionproject.core.common.Field
import org.ionproject.core.common.Media
import org.ionproject.core.common.Siren
import org.ionproject.core.common.SirenBuilder
import org.ionproject.core.mapEntries
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriTemplate
import java.net.URI

private val datatypeMapper = DatatypeMapper.forType(Media.MEDIA_SIREN)

private const val CALENDAR_CLASS = "calendar"

private val fields = listOf(
    Field(name = "type", type = "text", klass = "param/free-text-query"),
    Field(name = "startBefore", type = "date", klass = "param/date-query"),
    Field(name = "startAfter", type = "date", klass = "param/date-query"),
    Field(name = "endBefore", type = "date", klass = "param/date-query"),
    Field(name = "endAfter", type = "date", klass = "param/date-query"),
    Field(name = "summary", type = "text", klass = "param/free-text-query")
)

fun Calendar.toSiren(): Siren =
    SirenBuilder(properties)
        .klass(CALENDAR_CLASS)
        .action(
            Action(
                name = "search",
                title = "Search components",
                method = HttpMethod.GET,
                href = UriTemplate("${prod.value}/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}"),
                isTemplated = true,
                type = Media.APPLICATION_JSON,
                fields = fields
            )
        )
        .action(
            Action(
                name = "import",
                title = "Import Calendar",
                method = HttpMethod.GET,
                href = buildImportUrl(prod.value),
                isTemplated = true,
                type = Media.APPLICATION_JSON,
                fields = fields
            )
        )
        .link("self", href = URI("${prod.value}/calendar"))
        .link("about", href = URI("${prod.value}"))
        .toSiren()

fun buildImportUrl(identifier: Text): UriTemplate {
    // The prod.value was all combined in one string and we need to add the import after /v0/ and before the rest

    val id = identifier.value
    val idxVersionEnding = id.indexOf("/", 1)
    val version = id.substring(0, idxVersionEnding)
    val rest = id.substring(idxVersionEnding + 1, id.length)

    return UriTemplate("$version/import/$rest/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}")
}

fun CalendarComponent.toSiren(about: URI): Siren =
    SirenBuilder(sirenProperties)
        .klass(this::class.java.simpleName.lowercase())
        .link("self", href = URI.create("$about/calendar/${uid.value.value}"))
        .link("about", href = about)
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
        SUB_COMPONENTS to components.map(CalendarComponent::asSubComponentSirenProperties)
    )

private val CalendarComponent.sirenProperties: Map<String, Any>
    get() = mapOf(
        PROPERTIES to properties.toMap()
    )

private val CalendarComponent.asSubComponentSirenProperties: Map<String, Any>
    get() = mapOf(
        TYPE to this::class.java.simpleName.lowercase(),
        PROPERTIES to properties.toMap()
    )

private fun Iterable<Property>.toMap(): Map<String, Any> =
    this.groupBy { it.name }
        .mapEntries {
            val propName = it.key.lowercase()
            val list = it.value
            if (list.size > 1) {
                propName to list.map(Property::toSiren)
            } else {
                propName to list[0].toSiren()
            }
        }

private fun Property.toSiren(): Any {
    val aux = this as? ParameterizedProperty

    val params = aux?.parameters?.associate {
        it.name.lowercase() to it.toSiren()
    }

    val value = datatypeMapper.map(value)

    return if (params.isNullOrEmpty()) {
        object {
            val value = value
        }
    } else {
        object {
            val parameters = params
            val value = value
        }
    }
}

private fun PropertyParameter.toSiren(): Any {
    return if (values.isNotEmpty()) {
        if (values.size == 1) values[0]
        else values
    } else throw IllegalStateException("Instance of PropertyParameter with no values.")
}
