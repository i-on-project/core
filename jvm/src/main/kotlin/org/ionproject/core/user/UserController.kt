package org.ionproject.core.user

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.user.common.model.UserEditInput
import org.ionproject.core.user.common.repo.UserRepo
import org.ionproject.core.user.common.repr.toSirenRepresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val repo: UserRepo) {

    @GetMapping(Uri.users)
    fun getUsers(
        pagination: Pagination
    ): ResponseEntity<Siren> {
        val users = repo.getUsers(pagination)
        return ResponseEntity.ok(users.toSirenRepresentation(pagination))
    }

    @GetMapping(Uri.user)
    fun getUser(
        @PathVariable userId: String
    ): ResponseEntity<Siren> {
        val user = repo.getUser(userId)
        return ResponseEntity.ok(user.toSirenRepresentation())
    }

    @PutMapping(Uri.user)
    fun editUser(
        @PathVariable userId: String,
        @RequestBody input: UserEditInput
    ): ResponseEntity<Unit> {
        repo.editUser(userId, input)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.user)
    fun deleteUser(
        @PathVariable userId: String
    ): ResponseEntity<Siren> {
        repo.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }

}