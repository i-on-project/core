package org.ionproject.core.writeApi.insertClassSectionFaculty

interface InsertClassSectionFacultyRepo {
    fun insertClassSectionSchoolInfo(
        schoolName: String?,
        schoolAcr: String?,
        programmeName: String?,
        programmeAcr: String?,
        calendarTerm: String?,
        calendarSection: String?,
        language: String
    )

    fun insertClassSectionFacultyInfo(
        courseName: String?,
        courseAcr: String?,
        teachers: List<String>
    )
}
