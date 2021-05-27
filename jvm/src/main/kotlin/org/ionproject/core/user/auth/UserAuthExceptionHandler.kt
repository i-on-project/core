package org.ionproject.core.user.auth

import org.ionproject.core.user.auth.model.AuthError
import org.ionproject.core.user.auth.model.AuthErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserAuthExceptionHandler {

    @ExceptionHandler(value = [AuthRequestInvalidException::class, AuthRequestExpiredException::class])
    fun handleInvalidAuthRequest(
        ex: Exception
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.badRequest()
            .body(
                AuthErrorResponse(
                    AuthError.INVALID_REQUEST,
                    ex.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [AuthRequestPendingException::class])
    fun handleAuthRequestPending(
        ex: Exception
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                AuthErrorResponse(
                    AuthError.AUTHORIZATION_PENDING,
                    ex.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [AuthRequestInvalidScopesException::class])
    fun handleAuthRequestInvalidScopes(
        ex: Exception
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.badRequest()
            .body(
                AuthErrorResponse(
                    AuthError.INVALID_SCOPE,
                    ex.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [RefreshTokenRateLimitException::class])
    fun handleRefreshTokenRateLimit(
        ex: Exception
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body(
                AuthErrorResponse(
                    AuthError.SLOW_DOWN,
                    ex.localizedMessage
                )
            )
    }

    @ExceptionHandler(value = [UserTokenNotFoundException::class])
    fun handleUserNotFound(
        ex: Exception
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                AuthErrorResponse(
                    AuthError.INVALID_REQUEST,
                    ex.localizedMessage
                )
            )
    }
}
