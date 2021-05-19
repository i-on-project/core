package org.ionproject.core.userApi.user

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.common.accessControl.UserResource
import org.ionproject.core.userApi.common.accessControl.UserResourceOwner
import org.ionproject.core.userApi.common.accessControl.UserResourceScope
import org.ionproject.core.userApi.user.model.UserEditInput
import org.ionproject.core.userApi.user.repo.UserRepo
import org.ionproject.core.userApi.user.repr.toSirenRepresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val repo: UserRepo) {

    // TODO: add user home resource

    @GetMapping(Uri.user)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun getUser(
        @PathVariable @UserResourceOwner userId: String
    ): ResponseEntity<Siren> {
        val user = repo.getUser(userId)
        return ResponseEntity.ok(user.toSirenRepresentation())
    }

    @PutMapping(Uri.user)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun editUser(
        @PathVariable @UserResourceOwner userId: String,
        @RequestBody input: UserEditInput
    ): ResponseEntity<Unit> {
        repo.editUser(userId, input)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.user)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun deleteUser(
        @PathVariable @UserResourceOwner userId: String
    ): ResponseEntity<Siren> {
        repo.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }

}