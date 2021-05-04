package org.ionproject.core.programme.model

class Programme(
    val id: Int,
    val name: String,
    val acronym: String,
    val termSize: Int,
    val offers: MutableList<ProgrammeOffer>
)
