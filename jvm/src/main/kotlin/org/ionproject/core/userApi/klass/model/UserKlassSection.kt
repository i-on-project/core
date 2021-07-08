package org.ionproject.core.userApi.klass.model

data class UserKlassSection(
    val id: String,
    val classId: Int,
    val courseId: Int,
    val courseAcr: String,
    val courseName: String,
    val calendarTerm: String
)
