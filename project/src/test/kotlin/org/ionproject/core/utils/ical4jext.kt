package org.ionproject.core.utils

import net.fortuna.ical4j.model.*
import net.fortuna.ical4j.model.component.CalendarComponent

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