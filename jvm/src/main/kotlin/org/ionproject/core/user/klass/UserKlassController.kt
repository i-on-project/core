package org.ionproject.core.user.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserKlassController {

    @GetMapping(Uri.userClasses)
    fun getUserClasses(
        @PathVariable userId: String
    ): ResponseEntity<Siren> {
        TODO()
    }

    @PutMapping(Uri.userClasses)
    fun addUserClass(
        @PathVariable userId: String
    ): ResponseEntity<Siren> {
        TODO()
    }

    @GetMapping(Uri.userClass)
    fun getUserClass(
        @PathVariable userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        TODO()
    }

    @PutMapping(Uri.userClass)
    fun addUserClassSection(
        @PathVariable userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        TODO()
    }

    @DeleteMapping(Uri.userClass)
    fun deleteUserClass(
        @PathVariable userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        TODO()
    }

    @GetMapping(Uri.userClassSection)
    fun getUserClassSection(
        @PathVariable userId: String,
        @PathVariable classId: Int,
        @PathVariable sectionId: Int
    ): ResponseEntity<Siren> {
        TODO()
    }

    @DeleteMapping(Uri.userClassSection)
    fun deleteUserClassSection(
        @PathVariable userId: String,
        @PathVariable classId: Int,
        @PathVariable sectionId: Int
    ): ResponseEntity<Siren> {
        TODO()
    }

}