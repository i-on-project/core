package org.ionproject.core.writeApi.insertClassSectionEvents.json

import com.fasterxml.jackson.databind.JsonNode

class EventInfo(
    val title: String?,
    val description: String?,
    val location: List<String>?,
    val beginTime: String,
    val duration: String,
    val weekday: List<String>) {

    companion object {
        fun of (json: JsonNode): EventInfo {
            return EventInfo(
                json["title"]?.asText(),
                json["description"]?.asText(),
                json["location"]?.map { it.asText() },
                json["beginTime"].asText(),
                json["duration"].asText(),
                json["weekday"].map { it.asText() })
        }
    }
}

class CourseInfo(
    val name: String?,
    val acr: String?,
    val events: List<EventInfo>) {

    companion object {
        fun of (json: JsonNode): CourseInfo {
            val label = json["label"]

            return CourseInfo(
                label["name"]?.asText(),
                label["acr"]?.asText(),
                json["events"].map { EventInfo.of(it) })
        }
    }
}

class SchoolInfo(
    val schoolName: String?,
    val schoolAcr: String?,
    val programmeName: String?,
    val programmeAcr: String?,
    val programmeTermSize: Int?,
    val calendarTerm: String,
    val calendarSection: String,
    val courses: List<CourseInfo>
) {

    companion object {
        fun of(json: JsonNode): SchoolInfo {
            val schoolAcademicObj = json["school"]
            val programmeAcademicObj = json["programme"]

            return SchoolInfo(
                schoolAcademicObj["name"]?.asText(),
                schoolAcademicObj["acr"]?.asText(),
                programmeAcademicObj["name"]?.asText(),
                programmeAcademicObj["acr"]?.asText(),
                json["termSize"]?.asInt(),
                json["calendarTerm"].asText(),
                json["calendarSection"].asText(),
                json["courses"].map { CourseInfo.of(it) }
            )
        }
    }
}