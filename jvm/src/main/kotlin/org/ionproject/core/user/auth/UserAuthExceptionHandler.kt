package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthError
import org.ionproject.core.user.auth.model.AuthErrorResponse
import org.ionproject.core.user.auth.model.AuthTokenError
import org.ionproject.core.user.auth.model.TokenError
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserAuthExceptionHandler {

    @ExceptionHandler(value = [AuthInvalidRequestException::class])
    fun handleInvalidAuthRequest(
        ex: AuthInvalidRequestException
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                AuthErrorResponse(
                    AuthError.INVALID_REQUEST,
                    ex.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [RequestTokenInvalidRequestException::class])
    fun handleInvalidAuthRequestId(): ResponseEntity<AuthTokenError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(AuthTokenError(TokenError.INVALID_REQUEST))
    }

    @ExceptionHandler(value = [RequestTokenExpiredException::class])
    fun handleExpiredRequest(): ResponseEntity<AuthTokenError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(AuthTokenError(TokenError.EXPIRED_TOKEN))
    }

    @ExceptionHandler(value = [RequestTokenPendingException::class])
    fun handlePendingRequest(): ResponseEntity<AuthTokenError> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(AuthTokenError(TokenError.AUTHORIZATION_PENDING))
    }

}