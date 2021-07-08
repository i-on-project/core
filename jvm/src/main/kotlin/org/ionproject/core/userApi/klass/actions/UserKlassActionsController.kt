package org.ionproject.core.userApi.klass.actions

import org.ionproject.core.common.Siren
import org.ionproject.core.common.Uri
import org.ionproject.core.userApi.common.accessControl.UserResource
import org.ionproject.core.userApi.common.accessControl.UserResourceScope
import org.ionproject.core.userApi.klass.actions.repr.UserKlassActions
import org.ionproject.core.userApi.klass.actions.repr.UserKlassSectionActions
import org.ionproject.core.userApi.klass.repo.UserKlassRepo
import org.ionproject.core.userApi.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserKlassActionsController(val repo: UserKlassRepo) {

    @GetMapping(Uri.userClassActions)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getUserClassActions(
        user: User,
        @PathVariable classId: Int
    ): ResponseEntity<Siren> {
        val isSubscribed = repo.isSubscribedToClass(user, classId)
        return ResponseEntity.ok(UserKlassActions.toSirenRepresentation(classId, isSubscribed))
    }

    @GetMapping(Uri.userClassSectionActions)
    @UserResource(requiredScopes = [UserResourceScope.CLASS_SUBSCRIPTIONS])
    fun getUserClassSectionActions(
        user: User,
        @PathVariable classId: Int,
        @PathVariable sectionId: String
    ): ResponseEntity<Siren> {
        val isSubscribed = repo.isSubscribedToClassSection(user, classId, sectionId)
        return ResponseEntity.ok(UserKlassSectionActions.toSirenRepresentation(classId, sectionId, isSubscribed))
    }
}
