package org.ionproject.core.calendar.icalendar.properties.components.relationship

import org.ionproject.core.calendar.icalendar.properties.Property
import org.ionproject.core.calendar.icalendar.properties.parameters.*
import org.ionproject.core.calendar.icalendar.types.CalendarUserAddress

class Attendee(
    value: CalendarUserAddress,
    language: Language?,
    calendarUserType: CalendarUserType?,
    groupListMembership: GroupListMembership?,
    participationRole: ParticipationRole?,
    participationStatus: ParticipationStatus?,
    rsvpExpectation: RSVPExpectation?,
    delegatee: Delegatees?,
    delegators: Delegators?,
    sentBy: SentBy?,
    commonName: CommonName?,
    directoryEntryReference: DirectoryEntryReference?
) : Property(
    value,
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
) {
    override val name: String
        get() = iCalName

    companion object {
        private const val iCalName = "ATTENDEE"
    }
}