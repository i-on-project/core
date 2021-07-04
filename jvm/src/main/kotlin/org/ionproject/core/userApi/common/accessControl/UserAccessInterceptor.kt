package org.ionproject.core.userApi.common.accessControl

import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.userApi.auth.UserTokenNotFoundException
import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.ionproject.core.userApi.user.model.User
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAccessInterceptor(val repo: UserAuthRepo) : HandlerInterceptorAdapter() {

    companion object {
        private const val AUTH_HEADER_TYPE = "bearer"
        const val USER_INFO = "USER_INFO"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod)
            return true

        val resource = findUserResource(handler)
        if (resource != null) {
            val authHeader = request.getHeader("Authorization")
                ?: throw UnauthenticatedUserException("The resource you're trying to access is protected and requires authentication")

            val headerValue = authHeader.trim().split(" ")
            if (headerValue.size < 2)
                throw BadRequestException("Invalid authorization header format")

            if (headerValue[0].toLowerCase() != AUTH_HEADER_TYPE)
                throw BadRequestException("The authorization header type must be ${AUTH_HEADER_TYPE.toUpperCase()}")

            try {
                val token = headerValue[1]
                val user = repo.getUserByToken(token)
                    ?: throw UserTokenNotFoundException()

                checkAccessToResource(token, user, resource.requiredScopes)
                request.setAttribute(USER_INFO, user)
            } catch (ex: UserTokenNotFoundException) {
                throw UnauthenticatedUserException("The specified access token is invalid")
            }
        }

        return true
    }

    private fun checkAccessToResource(token: String, user: User, requiredScopes: Array<UserResourceScope>) {
        val info = repo.getTokenInfo(token)
        if (info.token.userId != user.userId)
            throw ForbiddenActionException("The specified access token does not have access to the user resources")

        if (info.token.accessTokenExpires.isBefore(Instant.now()))
            throw ForbiddenActionException("The provided access token has expired")

        repo.updateTokenUsedAt(token)

        val hasScopes = info.scopes
            .map { UserResourceScope.fromUserTokenScope(it) }
            .containsAll(requiredScopes.toSet())

        if (!hasScopes)
            throw ForbiddenActionException("The specified access token does not have access to this resource")
    }

    private fun findUserResource(handler: HandlerMethod): UserResource? =
        handler.getMethodAnnotation(UserResource::class.java)
}
