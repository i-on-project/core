package org.ionproject.core.insertClassSectionEvents

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
class InsertClassSectionEventsController(private val repo: InsertClassSectionEventsRepo) {

  @PutMapping("/v0/insertClassSectionEvents")
  fun insertClassSectionEvents(): ResponseEntity<Any> {

    val category = "Aulas"

    if (Calendar)

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
        "Aulas"
      )

      it.insertClassSectionEvent(
        "Software Laboratory",
        "SL",
        "1D",
        "1718v",
        "one sum",
        "desc",
        "pt-PT",
        "Aulas",
        Timestamp.valueOf("2020-07-01 10:00:00"),
        Timestamp.valueOf("2020-07-01 12:30:00"),
        "TU"
      )

    }

    return ResponseEntity.ok().build()
  }
}
