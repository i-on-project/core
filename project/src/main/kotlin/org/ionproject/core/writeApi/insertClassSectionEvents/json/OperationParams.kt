package org.ionproject.core.writeApi.insertClassSectionEvents.json

import com.fasterxml.jackson.databind.JsonNode
import java.sql.Timestamp

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
    val programmeTermSize: Int?,
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
                json["termSize"]?.asInt(),
                json["calendarTerm"].asText(),
                json["calendarSection"].asText(),
                json["courses"].map { CourseParams.of(it) },
                json["language"]?.asText() ?: "en-US"
            )
        }
    }
}

class CourseParams(
    val name: String?,
    val acr: String?,
    val events: List<EventParams>
) {

    companion object {
        fun of(json: JsonNode): CourseParams {
            val label = json["label"]

            return CourseParams(
                label["name"]?.asText(),
                label["acr"]?.asText(),
                json["events"].map { EventParams.of(it) })
        }
    }
}

class EventParams(
    val title: String?,
    val description: String?,
    val location: String?,
    val beginTime: Timestamp,
    val endTime: Timestamp,
    val weekdays: String?,
    val category: Int
) {

    companion object {
        fun of(json: JsonNode): EventParams {
            val beginTimeText = json["startDate"].asText()
            val durationText = json["endDate"].asText()
            val beginTimestamp = Timestamp.valueOf("${beginTimeText.replace("T", " ")}:00")
            val endTimestamp = Timestamp.valueOf("${durationText.replace("T", " ")}:00")

            return EventParams(
                json["title"]?.asText(),
                json["description"]?.asText(),
                json["location"]?.joinToString(",") { it.asText() },
                beginTimestamp,
                endTimestamp,
                json["weekday"]?.joinToString(",") { it.asText() },
                json["category"].asInt()
            )
        }
    }
}

