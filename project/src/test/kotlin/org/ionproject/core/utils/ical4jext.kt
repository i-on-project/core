package org.ionproject.core.utils

import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ComponentList
import net.fortuna.ical4j.model.Parameter
import net.fortuna.ical4j.model.ParameterList
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.PropertyList
import net.fortuna.ical4j.model.component.CalendarComponent
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

fun ParameterList(vararg parameters: Parameter) : ParameterList =
    ParameterList(false).apply {
        parameters.forEach {
            add(it)
        }
    }

fun PropertyList(vararg properties: Property) : PropertyList<Property> =
    PropertyList<Property>(properties.size).apply{
        properties.forEach {
            add(it)
        }
    }

fun ComponentList(vararg components: CalendarComponent): ComponentList<CalendarComponent> =
    ComponentList<CalendarComponent>(components.size).apply {
        addAll(components)
    }

fun Calendar.output(): String {
    val stream = ByteArrayOutputStream()

    CalendarOutputter().output(this, PrintWriter(stream))

    return stream.toString("utf-8")
}