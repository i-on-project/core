package org.ionproject.core.user.auth

import java.lang.RuntimeException

abstract class AuthInvalidRequestException(message: String) : RuntimeException(message)

// TODO: improve these exceptions and the associated handling

class InvalidClientIdException(
    clientId: String
) : AuthInvalidRequestException("The client_id $clientId is invalid!")

class InvalidNotificationMethodException(
    available: Iterable<String>
) : AuthInvalidRequestException("The specified notification method is invalid! Available methods: ${available.joinToString()}")

class InvalidUserCreationMethodException : AuthInvalidRequestException("A user cannot be created with the specified method!")

class InvalidScopesException(
    invalid: Iterable<String>
) : RuntimeException("The scopes ${invalid.joinToString()} are invalid")

class RequestTokenInvalidRequestException : RuntimeException()

class RequestTokenExpiredException : RuntimeException()

class RequestTokenPendingException : RuntimeException()

class RequestAlreadyValidatedException : RuntimeException()
