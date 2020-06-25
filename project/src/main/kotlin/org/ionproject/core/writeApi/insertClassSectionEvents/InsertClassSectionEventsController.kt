package org.ionproject.core.writeApi.insertClassSectionEvents

import com.fasterxml.jackson.databind.JsonNode
import org.ionproject.core.common.ProblemJson
import org.ionproject.core.writeApi.insertClassSectionEvents.json.SchoolInfo
import org.ionproject.core.writeApi.insertClassSectionEvents.json.SchemaValidator
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
class InsertClassSectionEventsController(private val repo: InsertClassSectionEventsRepo) {

  @PutMapping("/v0/insertClassSectionEvents", consumes = [ "application/json" ])
  fun insertClassSectionEvents(@RequestBody json: JsonNode): ResponseEntity<Any> {

    val errMessages = SchemaValidator.validate(json)
    if (errMessages.isNotEmpty()) {
      return ResponseEntity.badRequest().body(
        ProblemJson(
          "/err/write/insertClassSectionEvents/jsonSchemaConstraintViolation",
          "JSON Schema constraint violation.",
          400,
          "The provided request body was invalid for the insertClassSectionEvents' JSON Schema. Failed constraints: [ ${errMessages.reduce { acc, s -> "${acc}; $s" }} ]. This operation's JSON Schema: ${SchemaValidator.schemaDocUri}.",
          "/v0/insertClassSectionEvents"
        )
      )
    }

    val schoolInfo = SchoolInfo.of(json)
    schoolInfo.courses.forEach { co ->
      println(co.name)
      println(co.acr)
      co.events.forEach { println(it.beginTime); println(it.description) }
    }

    try {
      // Loop through the request's items and
      repo.transaction { sql ->
        sql.insertClassSectionSchoolInfo(
          schoolInfo.schoolName,
          schoolInfo.schoolAcr,
          schoolInfo.programmeName,
          schoolInfo.programmeAcr,
          schoolInfo.programmeTermSize,
          schoolInfo.calendarTerm
        )

        schoolInfo.courses.forEach { course ->
          sql.insertClassSectionCourseInfo(
            course.name,
            course.acr,
            schoolInfo.calendarSection,
            schoolInfo.calendarTerm,
            "pt-PT",
            "Aula"
          )

          course.events.forEach { event ->
            sql.insertClassSectionEvent(
              course.name,
              course.acr,
              schoolInfo.calendarSection,
              schoolInfo.calendarTerm,
              event.title,
              event.description,
              "pt-PT",
              "Aula",
              Timestamp.valueOf("2020-05-01 ${event.beginTime}"),
              Timestamp.valueOf("2020-05-01 18:30:00"),
              event.weekday.reduceRight { l, r -> "${l},${r}" } // [ "MO", "FR" ] -> "MO,FR"
            )
          }
        }
      }
    } catch (se: UnableToExecuteStatementException) {
      val ex = se.cause as PSQLException

      return ResponseEntity.badRequest().body(
        ProblemJson(
          "/err/write/constraint",
          "Transaction aborted",
          400,
          ex.serverErrorMessage.message,
          "/v0/insertClassSectionEvents"
        )
      )
    }
    return ResponseEntity.ok().build()
  }
}
