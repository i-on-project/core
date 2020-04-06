package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.RelationshipType
import org.ionproject.core.calendar.icalendar.types.Text

class RelatedTo(
    value: Text,
    relationshipType: RelationshipType?
) : Property(value, relationshipType) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "RELATED-TO"
    }
}