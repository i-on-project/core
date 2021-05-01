package org.ionproject.core.user.auth

import org.ionproject.core.common.ProblemJson
import org.ionproject.core.common.Uri
import org.ionproject.core.common.handleExceptionResponse
import org.ionproject.core.user.auth.model.AuthMethodInput
import org.ionproject.core.user.auth.model.AuthMethodResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import javax.servlet.http.HttpServletRequest

@RestController
class UserAuthController(val service: UserAuthService) {

    @ExceptionHandler(value = [InvalidClientIdException::class])
    fun handleInvalidClientId(
        req: HttpServletRequest,
        ex: InvalidClientIdException
    ): ResponseEntity<ProblemJson> = handleExceptionResponse(
        "https://github.com/i-on-project/core/docs/api/auth.md#invalid-client-id",
        "Invalid Client Id",
        400,
        ex.localizedMessage,
        req.requestURI
    )

    @ExceptionHandler(value = [InvalidNotificationMethodException::class])
    fun handleInvalidNotificationMethod(
        req: HttpServletRequest,
        ex: InvalidNotificationMethodException
    ): ResponseEntity<ProblemJson> = handleExceptionResponse(
        "https://github.com/i-on-project/core/docs/api/auth.md#invalid-notification-method",
        "Invalid Notification Method",
        400,
        ex.localizedMessage,
        req.requestURI
    )

    @GetMapping(Uri.authMethods, produces = ["application/json"])
    fun getAvailableMethods(): ResponseEntity<Iterable<AuthMethod>> {
        return ResponseEntity.ok(service.getAuthMethods())
    }

    @PostMapping(Uri.authMethods, produces = ["application/json"])
    fun selectAuthMethod(
        webRequest: WebRequest,
        @RequestBody methodInput: AuthMethodInput
    ): ResponseEntity<AuthMethodResponse> {
        val userAgent = webRequest.getHeader("User-Agent") ?: "Unknown"
        return ResponseEntity.ok(service.selectAuthMethod(userAgent, methodInput))
    }

    @GetMapping(Uri.authVerify)
    fun verifyUserAuth(
        @PathVariable reqId: String
    ) {

    }

    @PostMapping(Uri.authPoll)
    fun pollForUserAuth() {
    }
}
