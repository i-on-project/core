package org.ionproject.core.ingestion.model

data class SchoolCourseProgramme(
    val acronym: String,
    val termNumber: List<Int>,
    val optional: Boolean
)

data class SchoolCourse(
    val id: Int,
    val acronym: List<String>,
    val name: String,
    val termDuration: Int,
    val credits: Float,
    val scientificArea: String,
    val programmes: List<SchoolCourseProgramme>
)

data class SchoolCourses(
    val school: String,
    val courses: List<SchoolCourse>
)
