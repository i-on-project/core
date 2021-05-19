package org.ionproject.core.userApi.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.common.accessControl.UserResource
import org.ionproject.core.userApi.common.accessControl.UserResourceOwner
import org.ionproject.core.userApi.common.accessControl.UserResourceScope
import org.ionproject.core.userApi.klass.repo.UserKlassRepo
import org.ionproject.core.userApi.klass.repr.toSirenRepresentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserKlassController(val repo: UserKlassRepo) {

    @GetMapping(Uri.userClasses)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getSubscribedClasses(
        @PathVariable @UserResourceOwner userId: String,
        pagination: Pagination
    ): ResponseEntity<Siren> {
        val classes = repo.getSubscribedClasses(userId, pagination)
        return ResponseEntity.ok(classes.toSirenRepresentation(userId, pagination))
    }

    @GetMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getSubscribedClass(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        val klass = repo.getSubscribedClass(userId, classId)
        TODO()
    }

    @PutMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun subscribeToClass(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Unit> {
        val alreadySubscribed = repo.subscribeToClass(userId, classId)

        return if (alreadySubscribed)
            ResponseEntity.created(Uri.forUserClass(userId, classId)).build()
        else
            ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun unsubscribeFromClass(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int
    ): ResponseEntity<Unit> {
        repo.unsubscribeFromClass(userId, classId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getSubscribedClassSection(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Siren> {
        TODO()
    }

    @PutMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun subscribeToClassSection(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Siren> {
        TODO()
    }

    @DeleteMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun unsubscribeFromClassSection(
        @PathVariable @UserResourceOwner userId: String,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Siren> {
        TODO()
    }

}