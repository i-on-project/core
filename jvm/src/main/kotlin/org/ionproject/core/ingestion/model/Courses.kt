package org.ionproject.core.ingestion.model

data class SchoolCourse(
    val id: Int,
    val acronym: List<String>,
    val name: String,
    val termNumber: List<Int>,
    val termDuration: Int,
    val optional: Boolean,
    val ects: Int,
    val scientificArea: String?,
    val programmes: List<String>
)

data class SchoolCourses(
    val school: String,
    val courses: List<SchoolCourse>
)
