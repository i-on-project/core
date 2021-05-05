package org.ionproject.core.user.auth

import org.ionproject.core.common.Uri
import org.ionproject.core.user.auth.model.AuthError
import org.ionproject.core.user.auth.model.AuthErrorResponse
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthRequestAcknowledgement
import org.ionproject.core.user.auth.model.AuthSuccessfulResponse
import org.ionproject.core.user.auth.model.AuthTokenError
import org.ionproject.core.user.auth.model.AuthVerification
import org.ionproject.core.user.auth.model.TokenError
import org.ionproject.core.user.auth.registry.AuthMethod
import org.ionproject.core.user.auth.repo.UserAuthRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@RestController
class UserAuthController(val repo: UserAuthRepo) {

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

    @GetMapping(Uri.authMethods, produces = ["application/json"])
    fun getAvailableMethods(): ResponseEntity<Iterable<AuthMethod>> {
        return ResponseEntity.ok(repo.getAuthMethods())
    }

    @PostMapping(Uri.authMethods, produces = ["application/json"])
    fun selectAuthMethod(
        webRequest: WebRequest,
        @RequestBody methodInput: AuthMethodInput
    ): ResponseEntity<AuthRequestAcknowledgement> {
        val userAgent = webRequest.getHeader("User-Agent") ?: "Unknown"
        return ResponseEntity.ok(repo.addAuthRequest(userAgent, methodInput))
    }

    @PostMapping(Uri.authVerify)
    fun verifyUserAuth(
        @RequestBody verification: AuthVerification
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(repo.verifyAuthRequest(verification.authReqId, verification.secret))
    }

    @GetMapping(Uri.authPoll)
    fun pollForUserAuth(
        @PathVariable reqId: String
    ): ResponseEntity<AuthSuccessfulResponse> {
        return ResponseEntity.ok(repo.checkAuthRequest(reqId))
    }
}
