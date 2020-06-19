package org.ionproject.core.writeApi.insertClassSectionEvents

import org.ionproject.core.common.ProblemJson
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.postgresql.util.PSQLException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
class InsertClassSectionEventsController(private val repo: InsertClassSectionEventsRepo) {

  @PutMapping("/v0/insertClassSectionEvents")
  fun insertClassSectionEvents(): ResponseEntity<Any> {
    try {
      repo.transaction {
        it.insertClassSectionSchoolInfo(
          "school",
          "ISEL",
          "Leica",
          "LEIC",
          3,
          "1718v"
        )

        it.insertClassSectionCourseInfo(
          "Software Laboratory",
          "SL",
          "1D",
          "1718v",
          "pt-PT",
          "Aula"
        )

        it.insertClassSectionEvent(
          "Software Laboratory",
          "SL",
          "1D",
          "1718v",
          "one sum",
          "desc palavra",
          "pt-PT",
          "Aula",
          Timestamp.valueOf("2020-07-01 10:00:00"),
          Timestamp.valueOf("2020-07-01 12:30:00"),
          "TU"
        )

      }
    } catch (se: UnableToExecuteStatementException) {
      val ex = se.cause as PSQLException

      return ResponseEntity.badRequest().body(
        ProblemJson(
          "/err/write/constraint",
          "The given parameters do not abide by the constraints",
          400,
          ex.serverErrorMessage.message,
          "/v0/insertClassSectionEvents"
        )
      )
    }
    return ResponseEntity.ok().build()
  }
}
