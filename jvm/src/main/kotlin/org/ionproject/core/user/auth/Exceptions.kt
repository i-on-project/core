package org.ionproject.core.user.auth

import java.lang.RuntimeException

abstract class AuthInvalidRequestException(message: String) : RuntimeException(message)

class InvalidClientIdException(
    clientId: String
) : AuthInvalidRequestException("The client_id $clientId is invalid!")

class InvalidNotificationMethodException(
    available: Set<String>
) : AuthInvalidRequestException("The specified notification method is invalid! Available methods: ${available.joinToString()}")

class RequestTokenInvalidRequestException : RuntimeException()

class RequestTokenExpiredException : RuntimeException()

class RequestTokenPendingException : RuntimeException()
