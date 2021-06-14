package org.ionproject.core.userApi.auth

import java.lang.RuntimeException

/*
    Auth Request Exceptions
 */

abstract class AuthRequestInvalidException(message: String) : RuntimeException(message)

class AuthRequestInvalidClientIdException(
    clientId: String
) : AuthRequestInvalidException("The client_id $clientId is invalid!")

class AuthRequestInvalidNotificationMethodException(
    available: Iterable<String>
) : AuthRequestInvalidException("The specified notification method is invalid! Available methods: ${available.joinToString()}")

class AuthRequestAlreadyExistsException(
    email: String
) : AuthRequestInvalidException("An auth request already exists for $email with the specified client")

class AuthRequestNotFoundException : AuthRequestInvalidException("The specified auth request wasn't found")

class AuthRequestInvalidSecretException : AuthRequestInvalidException("The auth request has been cancelled because of an incorrect secret token")

class AuthRequestAlreadyVerifiedException : AuthRequestInvalidException("This auth request has already been validated")

class AuthRequestUserCreationException : AuthRequestInvalidException("A user cannot be created with the specified method!")

class AuthRequestExpiredException : RuntimeException("The specified auth request has expired")

class AuthRequestPendingException : RuntimeException("The auth request is still waiting for a response")

class AuthRequestInvalidScopesException(
    invalid: Iterable<String>
) : RuntimeException("The scopes ${invalid.joinToString()} are invalid")

/*
    User Token Exceptions
 */

class RefreshTokenRateLimitException(
    rateMinutes: Long
) : RuntimeException("You can only refresh this token once each $rateMinutes minutes")

class UserTokenNotFoundException : RuntimeException("The specified user token wasn't found")

class InvalidRefreshToken : RuntimeException("The specified refresh token is not valid for the access token")
