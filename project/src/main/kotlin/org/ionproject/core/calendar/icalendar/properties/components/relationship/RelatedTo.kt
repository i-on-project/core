package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.properties.parameters.RelationshipType
import org.ionproject.core.calendar.icalendar.types.ICalendarDataType
import org.ionproject.core.calendar.icalendar.types.Text
import org.ionproject.core.calendar.toText

class RelatedTo(
    value: String,
    val relationshipType: RelationshipType?
) : ParameterizedProperty {

    override val value: Text = value.toText()

    override val parameters: List<PropertyParameter> = listOfNotNull(relationshipType)

    override val name: String
        get() = "RELATED-TO"
}