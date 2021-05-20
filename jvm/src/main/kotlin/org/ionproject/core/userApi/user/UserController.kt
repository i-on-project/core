package org.ionproject.core.userApi.user

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.common.accessControl.UserResource
import org.ionproject.core.userApi.common.accessControl.UserResourceScope
import org.ionproject.core.userApi.user.model.User
import org.ionproject.core.userApi.user.model.UserEditInput
import org.ionproject.core.userApi.user.repo.UserRepo
import org.ionproject.core.userApi.user.repr.toSirenRepresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val repo: UserRepo) {

    // TODO: add user home resource

    @GetMapping(Uri.userBase)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun getUser(
        user: User
    ): ResponseEntity<Siren> {
        return ResponseEntity.ok(user.toSirenRepresentation())
    }

    @PutMapping(Uri.userBase)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun editUser(
        user: User,
        @RequestBody input: UserEditInput
    ): ResponseEntity<Unit> {
        repo.editUser(user.userId, input)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.userBase)
    @UserResource(requiredScopes = [UserResourceScope.PROFILE])
    fun deleteUser(
        user: User
    ): ResponseEntity<Siren> {
        repo.deleteUser(user.userId)
        return ResponseEntity.noContent().build()
    }
}
