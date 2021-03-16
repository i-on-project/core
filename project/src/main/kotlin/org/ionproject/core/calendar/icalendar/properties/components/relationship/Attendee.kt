package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.ParameterizedProperty
import org.ionproject.core.calendar.icalendar.properties.parameters.CalendarUserType
import org.ionproject.core.calendar.icalendar.properties.parameters.CommonName
import org.ionproject.core.calendar.icalendar.properties.parameters.Delegatees
import org.ionproject.core.calendar.icalendar.properties.parameters.Delegators
import org.ionproject.core.calendar.icalendar.properties.parameters.DirectoryEntryReference
import org.ionproject.core.calendar.icalendar.properties.parameters.GroupListMembership
import org.ionproject.core.calendar.icalendar.properties.parameters.Language
import org.ionproject.core.calendar.icalendar.properties.parameters.ParticipationRole
import org.ionproject.core.calendar.icalendar.properties.parameters.ParticipationStatus
import org.ionproject.core.calendar.icalendar.properties.parameters.PropertyParameter
import org.ionproject.core.calendar.icalendar.properties.parameters.RSVPExpectation
import org.ionproject.core.calendar.icalendar.properties.parameters.SentBy
import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class Attendee(
    override val value: CalendarUserAddress,
    val language: Language? = null,
    val calendarUserType: CalendarUserType? = null,
    val groupListMembership: GroupListMembership? = null,
    val participationRole: ParticipationRole? = null,
    val participationStatus: ParticipationStatus? = null,
    val rsvpExpectation: RSVPExpectation? = null,
    val delegatee: Delegatees? = null,
    val delegators: Delegators? = null,
    val sentBy: SentBy? = null,
    val commonName: CommonName? = null,
    val directoryEntryReference: DirectoryEntryReference? = null
) : ParameterizedProperty {

    override val parameters: List<PropertyParameter> = listOfNotNull(
        language,
        calendarUserType,
        groupListMembership,
        participationRole,
        participationStatus,
        rsvpExpectation,
        delegatee,
        delegators,
        sentBy,
        commonName,
        directoryEntryReference
    )

    override val name: String
        get() = "ATTENDEE"
}
