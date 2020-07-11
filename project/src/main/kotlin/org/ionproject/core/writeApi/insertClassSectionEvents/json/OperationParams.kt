package org.ionproject.core.writeApi.insertClassSectionEvents.json

import com.fasterxml.jackson.databind.JsonNode
import org.ionproject.core.asSanitizedText
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
            val beginTimestamp: Timestamp
            val endTimestamp: Timestamp

            val beginTimeText = json["beginTime"]?.asText()
            if (beginTimeText != null) {
                // Recurrent events
                val durationText = json["duration"].asText()
                // TODO: ClassSection's start and finish dates are unknown, cannot infer event's date
                beginTimestamp = Timestamp.valueOf("2020-05-01 ${beginTimeText}:00")
                    // Date value will be ignored. Only the Time portion will be taken into account.
                endTimestamp = Timestamp(
                   beginTimestamp.time + Timestamp.valueOf("2020-01-01 ${durationText}:00").time
                )
            } else {
                // Non recurrent events
                val startDateText = json["startDate"].asText()
                val endDateText = json["endDate"].asText()
                beginTimestamp = Timestamp.valueOf("${startDateText.replace("T", " ")}:00")
                endTimestamp = Timestamp.valueOf("${endDateText.replace("T", " ")}:00")
            }

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

