package org.ionproject.core.userApi.common.accessControl

import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.userApi.auth.UserTokenNotFoundException
import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.RuntimeException
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAccessInterceptor(val repo: UserAuthRepo) : HandlerInterceptorAdapter() {

    companion object {
        private const val AUTH_HEADER_TYPE = "bearer"
        private const val SPRING_PATH_VARIABLE_MAP = "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod)
            return true

        val resource = findUserResource(handler)
        if (resource != null) {
            val parameter = handler.methodParameters
                .firstOrNull {
                    it.hasParameterAnnotation(UserResourceOwner::class.java)
                            && it.hasParameterAnnotation(PathVariable::class.java)
                }

            val pathVariableMap = request.getAttribute(SPRING_PATH_VARIABLE_MAP) as Map<*, *>
            val userId = pathVariableMap[parameter?.parameter?.name] as String?
                ?: throw ResourceOwnerParameterNotFound()

            val authHeader = request.getHeader("Authorization")
                ?: throw UnauthenticatedUserException("The resource you're trying to access is protected and requires authentication")

            val headerValue = authHeader.trim().split(" ")
            if (headerValue.size < 2)
                throw BadRequestException("Invalid authorization header format")

            if (headerValue[0].toLowerCase() != AUTH_HEADER_TYPE)
                throw BadRequestException("The authorization header type must be ${AUTH_HEADER_TYPE.toUpperCase()}")

            checkAccessToResource(headerValue[1], userId, resource.requiredScopes)
        }

        return true
    }

    private fun checkAccessToResource(token: String, userId: String, requiredScopes: Array<UserResourceScope>) {
        try {
            val info = repo.getTokenInfo(token)
            if (info.token.userId != userId)
                throw ForbiddenActionException("The specified access token does not have access to the user resources")

            if (info.token.accessTokenExpires.isBefore(Instant.now()))
                throw ForbiddenActionException("The provided access token has expired")

            val hasScopes = info.scopes
                .map { UserResourceScope.fromUserTokenScope(it) }
                .containsAll(requiredScopes.toSet())

            if (!hasScopes)
                throw ForbiddenActionException("The specified access token does not have access to this resource")
        } catch (ex: UserTokenNotFoundException) {
            throw UnauthenticatedUserException("The specified access token is invalid")
        }
    }

    private fun findUserResource(handler: HandlerMethod): UserResource? =
        handler.getMethodAnnotation(UserResource::class.java)

}

class ResourceOwnerParameterNotFound : RuntimeException("A UserResource handler must receive one UserResourceOwner that is a path variable")