package org.ionproject.core.ingestion.processor.sql.model

import org.ionproject.core.ingestion.model.SchoolCourse
import org.ionproject.core.ingestion.model.SchoolCourseProgramme

data class RealCourse(
    val id: Int,
    val acronym: String,
    val name: String,
    val credits: Float,
    val scientificArea: String,
    val termDuration: Int
)

fun SchoolCourse.toRealCourse() = RealCourse(
    id,
    acronym[0],
    name,
    credits,
    scientificArea,
    termDuration
)

fun SchoolCourse.isCourseInfoDifferent(realCourse: RealCourse) =
    toRealCourse() != realCourse

data class RealAlternativeCourseAcronym(
    val courseId: Int,
    val acronym: String
)

fun SchoolCourse.toAlternativeCourseAcronyms(courseId: Int) = acronym.drop(1).map {
    RealAlternativeCourseAcronym(courseId, it)
}

data class RealProgrammeOffer(
    val id: Int,
    val programmeId: Int,
    val courseId: Int,
    val optional: Boolean
)

fun SchoolCourseProgramme.isProgrammeOfferInfoDifferent(realProgrammeOffer: RealProgrammeOffer) =
    optional != realProgrammeOffer.optional

data class RealProgrammeOfferTerm(
    val offerId: Int,
    val termNumber: Int
)
