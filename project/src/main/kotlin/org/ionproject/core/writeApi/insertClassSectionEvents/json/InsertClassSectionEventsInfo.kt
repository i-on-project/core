package org.ionproject.core.writeApi.insertClassSectionEvents.json

import com.fasterxml.jackson.databind.JsonNode
import java.sql.Timestamp

class EventInfo(
  val title: String?,
  val description: String?,
  val location: List<String>?,
  val beginTime: Timestamp,
  val endTime: Timestamp,
  val weekday: List<String>
) {

  companion object {
    fun of(json: JsonNode): EventInfo {
      val beginTimeText = json["beginTime"].asText()
      val durationText = json["duration"].asText()
      val beginTimestamp = Timestamp.valueOf("2020-05-01 ${beginTimeText}:00")

      // Date value will be ignored. Only the Time portion will be taken into account.
      val endTimestamp = Timestamp(
        beginTimestamp.time + Timestamp.valueOf("2020-01-01 ${durationText}:00").time
      )

      return EventInfo(
        json["title"]?.asText(),
        json["description"]?.asText(),
        json["location"]?.map { it.asText() },
        beginTimestamp,
        endTimestamp,
        json["weekday"].map { it.asText() })
    }
  }

  override fun toString(): String {
    return "title: ${title}; description: ${description};" +
        " location: ${location?.reduceRight { l, r -> "${l},${r}" }}; beginTime: ${beginTime};" +
        " duration: ${endTime.time - beginTime.time}; weekday: ${weekday.reduceRight { l, r -> "${l},${r}" }};"
  }
}

class CourseInfo(
  val name: String?,
  val acr: String?,
  val events: List<EventInfo>
) {

  companion object {
    fun of(json: JsonNode): CourseInfo {
      val label = json["label"]

      return CourseInfo(
        label["name"]?.asText(),
        label["acr"]?.asText(),
        json["events"].map { EventInfo.of(it) })
    }
  }

  override fun toString(): String {
    return "name: ${name}, acr: ${acr}; events: $events;"
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

  override fun toString(): String {
    return "schoolName: ${schoolName}; schoolAcr: ${schoolAcr};" +
        " programmeName: ${programmeName}; programmeAcr: ${programmeAcr};" +
        " programmeTermSize: ${programmeTermSize}; calendarSection: ${calendarSection};" +
        " calendarTerm: ${calendarTerm}; courses: $courses;"
  }
}