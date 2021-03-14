package org.ionproject.core.writeApi.insertClassSectionFaculty

import org.springframework.stereotype.Component

@Component
class InsertClassSectionFacultyRepoImpl : InsertClassSectionFacultyRepo {
    override fun insertClassSectionSchoolInfo(
        schoolName: String?,
        schoolAcr: String?,
        programmeName: String?,
        programmeAcr: String?,
        calendarTerm: String?,
        calendarSection: String?,
        language: String
    ) {
        // sink
    }

    override fun insertClassSectionFacultyInfo(courseName: String?, courseAcr: String?, teachers: List<String>) {
        // sink
    }
}
