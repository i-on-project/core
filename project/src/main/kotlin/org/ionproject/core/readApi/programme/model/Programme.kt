package org.ionproject.core.readApi.programme.model

class Programme(
    val id: Int,
    val name: String? = "",
    val acronym: String,
    val termSize: Int,
    val offers: MutableList<ProgrammeOffer>
)
