package org.ionproject.core.readApi.programme.model

class ProgrammeOffer(
    val id: Int,
    val courseAcr: String,
    val programmeId: Int,
    val courseId: Int,
    val termNumber: Int,
    val optional: Boolean
)
