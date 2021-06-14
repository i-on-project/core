package org.ionproject.core.userApi.common.accessControl

import org.ionproject.core.userApi.user.model.UserTokenScope

enum class UserResourceScope(val scope: String) {
    PROFILE("profile"),
    CLASS_SUBSCRIPTIONS("classes");

    companion object {
        fun fromUserTokenScope(uts: UserTokenScope) =
            values().first { it.scope == uts.scopeId }
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class UserResource(val requiredScopes: Array<UserResourceScope>)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class UserResourceOwner
