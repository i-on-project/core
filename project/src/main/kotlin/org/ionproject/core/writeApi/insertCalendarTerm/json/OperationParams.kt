package org.ionproject.core.writeApi.insertCalendarTerm.json

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
    val startDate: String?,
    val endDate: String?,
    val language: String?,
    val calendarTerm: String?,
    val intervals: List<IntervalParams>
) {

    companion object {
        fun of(json: JsonNode): OperationParams {
            val schoolAcademicObj = json["school"]

            return OperationParams(
                schoolAcademicObj["name"]?.asText(),
                schoolAcademicObj["acr"]?.asText(),
                json["startDate"]?.asText(),
                json["endDate"]?.asText(),
                json["language"]?.asText(),
                json["calendarTerm"].asSanitizedText(),
                json["intervals"].map { IntervalParams.of(it) }
            )
        }
    }
}

class IntervalParams(
    val startDate: String?,
    val endDate: String?,
    val name: String?,
    val categories: List<Int>?,
    val excludes: List<Int>?,
    val curricularTerm: List<Int>?
) {
    companion object {
        fun of(json: JsonNode): IntervalParams {
            return IntervalParams(
                json["startDate"]?.asText(),
                json["endDate"]?.asText(),
                json["name"]?.asText(),
                json["categories"]?.map { it.asInt() },
                json["excludes"]?.map { it.asInt() },
                json["curricularTerm"]?.map { it.asInt() }
            )
        }
    }
}
