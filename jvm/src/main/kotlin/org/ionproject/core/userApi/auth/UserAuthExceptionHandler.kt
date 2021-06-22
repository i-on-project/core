package org.ionproject.core.userApi.auth

import org.ionproject.core.userApi.auth.model.AuthErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserAuthExceptionHandler {

    @ExceptionHandler(value = [AuthErrorException::class])
    fun handleAuthError(
        ex: AuthErrorException
    ): ResponseEntity<AuthErrorResponse> {
        return ResponseEntity.badRequest()
            .body(ex.getErrorResponse())
    }
}
