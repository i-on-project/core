package org.ionproject.core.ingestion.model

data class SchoolProgramme(
    val acronym: String,
    val name: String,
    val termSize: Int,
    val department: String?,
    val coordination: List<String> = listOf(),
    val email: String?,
    val uri: String?,
    val description: String?
)

data class SchoolProgrammes(
    val school: String,
    val programmes: List<SchoolProgramme>
)