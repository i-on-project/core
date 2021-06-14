package org.ionproject.core.userApi.klass

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.userApi.common.accessControl.UserResource
import org.ionproject.core.userApi.common.accessControl.UserResourceScope
import org.ionproject.core.userApi.klass.repo.UserKlassRepo
import org.ionproject.core.userApi.klass.repr.toSirenRepresentation
import org.ionproject.core.userApi.user.model.User
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
        user: User,
        pagination: Pagination
    ): ResponseEntity<Siren> {
        val classes = repo.getSubscribedClasses(user.userId, pagination)
        return ResponseEntity.ok(classes.toSirenRepresentation(user.userId, pagination))
    }

    @GetMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getSubscribedClass(
        user: User,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        val klass = repo.getSubscribedClass(user.userId, classId)
        return ResponseEntity.ok(klass.toSirenRepresentation())
    }

    @PutMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun subscribeToClass(
        user: User,
        @PathVariable classId: Int
    ): ResponseEntity<Unit> {
        val alreadySubscribed = repo.subscribeToClass(user.userId, classId)

        return if (alreadySubscribed)
            ResponseEntity.created(Uri.forUserClass(classId)).build()
        else
            ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.userClass)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun unsubscribeFromClass(
        user: User,
        @PathVariable classId: Int
    ): ResponseEntity<Unit> {
        repo.unsubscribeFromClass(user.userId, classId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getSubscribedClassSection(
        user: User,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Siren> {
        val klassSection = repo.getSubscribedClassSection(user.userId, classId, sectionId)
        return ResponseEntity.ok(klassSection.toSirenRepresentation(classId))
    }

    @PutMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun subscribeToClassSection(
        user: User,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Unit> {
        val alreadySubscribed = repo.subscribeToClassSection(user.userId, classId, sectionId)

        return if (alreadySubscribed)
            ResponseEntity.created(Uri.forUserClassSection(classId, sectionId)).build()
        else
            ResponseEntity.noContent().build()
    }

    @DeleteMapping(Uri.userClassSection)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun unsubscribeFromClassSection(
        user: User,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Unit> {
        repo.unsubscribeFromClassSection(user.userId, classId, sectionId)
        return ResponseEntity.noContent().build()
    }
}
