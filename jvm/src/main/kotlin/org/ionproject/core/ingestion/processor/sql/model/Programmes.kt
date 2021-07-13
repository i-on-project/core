package org.ionproject.core.ingestion.processor.sql.model

import org.ionproject.core.ingestion.model.SchoolProgramme

data class RealSchoolProgramme(
    val id: Int,
    val acronym: String,
    val name: String,
    val termSize: Int,
    val department: String?,
    val email: String?,
    val uri: String?,
    val description: String?,
    val available: Boolean = true
)

fun SchoolProgramme.toRealProgramme(id: Int = 0) = RealSchoolProgramme(
    id,
    acronym,
    name,
    termSize,
    department,
    email,
    uri,
    description
)

fun SchoolProgramme.isProgrammeInfoDifferent(realProgramme: RealSchoolProgramme) =
    toRealProgramme(realProgramme.id) != realProgramme

data class RealProgrammeCoordinator(
    val id: Int = 0,
    val programmeId: Int,
    val name: String
)

fun SchoolProgramme.toCoordinators(programmeId: Int) = coordination.map {
    RealProgrammeCoordinator(0, programmeId, it)
}
