package org.ionproject.core.writeApi.insertClassSectionEvents

import com.fasterxml.jackson.databind.JsonNode
import org.ionproject.core.common.Media
import org.ionproject.core.common.ProblemJson
import org.ionproject.core.join
import org.ionproject.core.writeApi.common.Uri
import org.ionproject.core.writeApi.insertClassSectionEvents.json.SchemaValidator
import org.ionproject.core.writeApi.insertClassSectionEvents.json.InsertClassSectionEventsParams
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InsertClassSectionEventsController(private val repo: InsertClassSectionEventsRepo) {

  @PutMapping(Uri.insertClassSectionEvents, consumes = [Media.APPLICATION_JSON])
  fun insertClassSectionEvents(@RequestBody json: JsonNode): ResponseEntity<Any> {

    /**
     * Analyze the received JSON object in comparison with the defined
     * JSON Schema (statically defined JSON object which enforces syntax and semantic restrictions on the requests).
     */
    val errMessages = SchemaValidator.validate(json)
    if (errMessages.isNotEmpty()) {
      // Invalid JSON received
      return ResponseEntity.badRequest().body(
        ProblemJson(
          "/err/write/insertClassSectionEvents/jsonSchemaConstraintViolation",
          "JSON Schema constraint violation.",
          400,
          "The provided request body was invalid for the insertClassSectionEvents' JSON Schema. Failed constraints: [ ${errMessages.join(
            ";"
          )} ]. This operation's JSON Schema: ${SchemaValidator.schemaDocUri}.",
          "/v0/insertClassSectionEvents"
        )
      )
    }

    // Translate the JSON Node into a more easier to use/read object
    val schoolInfo = InsertClassSectionEventsParams.of(json)
    try {
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
            schoolInfo.language,
            schoolInfo.category
          )

          course.events.forEach { event ->
            // Mandatory iCalendar component properties
            // Give default values in case these weren't included in the request
            val eventTitle = event.title ?: "${course.acr} ${schoolInfo.category}"
            val eventDescription = event.description
              ?: "Event '${schoolInfo.category}' during ${schoolInfo.calendarTerm} for the Class ${schoolInfo.calendarSection}."

            sql.insertClassSectionEvent(
              course.name,
              course.acr,
              schoolInfo.calendarSection,
              schoolInfo.calendarTerm,
              eventTitle,
              eventDescription,
              schoolInfo.language,
              schoolInfo.category,
              event.beginTime,
              event.endTime,
              event.weekdays,
              event.location
            )
          }
        }
      }
    } catch (se: UnableToExecuteStatementException) {
      // PostgreSQL reported an error and the transaction was aborted...
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