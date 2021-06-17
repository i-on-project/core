package org.ionproject.core.userApi.auth

import org.ionproject.core.userApi.auth.model.AuthError
import org.ionproject.core.userApi.auth.model.AuthErrorResponse
import java.lang.RuntimeException

abstract class AuthErrorException(message: String, val error: AuthError) : RuntimeException(message) {
    fun getErrorResponse() = AuthErrorResponse(
        error,
        localizedMessage
    )
}

// invalid_client
class AuthRequestInvalidClientException : AuthErrorException(
    "The specified client_id/client_secret is invalid!",
    AuthError.INVALID_GRANT
)

// unauthorized_client
class AuthRequestUnauthorizedClientException : AuthErrorException(
    "The specified client is unauthorized to access this resource",
    AuthError.UNAUTHORIZED_CLIENT
)

// invalid_grant
open class AuthGrantException(message: String) : AuthErrorException(message, AuthError.INVALID_GRANT)
class GrantInvalidAuthRequestException : AuthGrantException("The specified auth request is invalid")
class GrantInvalidRefreshTokenException : AuthGrantException("The specified refresh token is invalid")

// unsupported grant type
class UnsupportedGrantTypeException : AuthErrorException(
    "The specified grant type is not supported",
    AuthError.UNSUPPORTED_GRANT_TYPE
)

// invalid_request
open class AuthRequestInvalidException(message: String) : AuthErrorException(message, AuthError.INVALID_REQUEST)
class AuthRequestAlreadyExistsException : AuthRequestInvalidException("An auth request is already in progress")
class AuthRequestNotFoundException : AuthRequestInvalidException("The specified auth request wasn't found")
class AuthRequestInvalidSecretException : AuthRequestInvalidException("The auth request has been cancelled because of an incorrect secret token")
class AuthRequestAlreadyVerifiedException : AuthRequestInvalidException("This auth request has already been validated")
class UserTokenNotFoundException : AuthRequestInvalidException("The specified user token wasn't found")

// expired_token
class AuthRequestExpiredException : AuthErrorException(
    "The specified auth request has expired",
    AuthError.EXPIRED_TOKEN
)

// authorization_pending
class AuthRequestPendingException : AuthErrorException(
    "The auth request is still waiting for a response",
    AuthError.AUTHORIZATION_PENDING
)

// invalid_scope
class AuthRequestInvalidScopesException(invalid: Iterable<String>) : AuthErrorException(
    "The scopes ${invalid.joinToString()} are invalid",
    AuthError.INVALID_SCOPE
)

// slow_down
class RefreshTokenRateLimitException(rateMinutes: Long) : AuthErrorException(
    "You can only refresh this token once each $rateMinutes minutes",
    AuthError.SLOW_DOWN
)
