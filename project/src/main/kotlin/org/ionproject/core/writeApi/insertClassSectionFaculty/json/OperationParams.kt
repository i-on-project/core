package org.ionproject.core.writeApi.insertClassSectionFaculty.json

import com.fasterxml.jackson.databind.JsonNode
import org.ionproject.core.asSanitizedText

/**
 * Extract all values from the controller's JSON Node parameter.
 *
 * These will prepare the values (e.g. setting default values or calculating timestamps)
 * for the upcoming stored procedure calls.
 */
class OperationParams(
    val schoolName: String?,
    val schoolAcr: String?,
    val programmeName: String?,
    val programmeAcr: String?,
    val calendarTerm: String,
    val calendarSection: String,
    val courses: List<CourseParams>,
    val language: String
) {

    companion object {
        fun of(json: JsonNode): OperationParams {
            val schoolAcademicObj = json["school"]
            val programmeAcademicObj = json["programme"]

            return OperationParams(
                schoolAcademicObj["name"]?.asText(),
                schoolAcademicObj["acr"]?.asText(),
                programmeAcademicObj["name"]?.asText(),
                programmeAcademicObj["acr"]?.asText(),
                json["calendarTerm"].asSanitizedText(),
                json["calendarSection"].asSanitizedText(),
                json["courses"].map { CourseParams.of(it) },
                json["language"]?.asText() ?: "en-US"
            )
        }
    }
}

class CourseParams(
    val name: String?,
    val acr: String?,
    val teachers: List<TeacherParams>
) {

    companion object {
        fun of(json: JsonNode): CourseParams {
            val label = json["label"]

            return CourseParams(
                label["name"]?.asText(),
                label["acr"]?.asText(),
                json["teachers"].map { TeacherParams.of(it) }
            )
        }
    }
}

class TeacherParams(
    val name: String
) {

    companion object {
        fun of(json: JsonNode): TeacherParams {
            return TeacherParams(
                json["name"].asText()
            )
        }
    }
}
