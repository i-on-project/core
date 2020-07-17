package org.ionproject.core.programme.model

class ProgrammeOffer(
    val id: Int,
    val courseAcr: String,
    val programmeId: Int,
    val courseId: Int,
    val termNumber: List<Int>,
    val optional: Boolean
)
