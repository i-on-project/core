package org.ionproject.core.common

import org.ionproject.core.common.customExceptions.*
import org.postgresql.util.PSQLException
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

/**
 * Class that handles global exceptions that may occur
 * in several handlers, for example 'LangNumberFormatException'
 * when a parameter is not a integer or is a string instead of a number,
 * or Unauthenticated Access to a endpoint that requires so, ...
 *
 * This handling replaces 'Jackson' default messages when an exception
 * is thrown.
 */
@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [BadRequestException::class])
    private fun handleBadRequestException(
      ex: BadRequestException,
      request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
          "",
          "Bad request",
          400,
          ex.localizedMessage,
          request.requestURI
        )
    }

    @ExceptionHandler(value = [PSQLException::class])
    private fun handleUnableToCreateStatementException(
            ex: PSQLException,
            request: HttpServletRequest
    ) : ResponseEntity<ProblemJson> {
        return handleResponse(
                "",
                "Internal Error (DB)",
                500,
                ex.localizedMessage,
                request.requestURI
        )
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    private fun handleInternalServerErrorException(
            ex: InternalServerErrorException,
            request: HttpServletRequest
    ) : ResponseEntity<ProblemJson> {
        return handleResponse(
                "",
                "Internal Error",
                500,
                ex.localizedMessage,
                request.requestURI
        )
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    private fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
            "",
            "Resource not found",
            404,
            ex.localizedMessage,
            request.requestURI
        )
    }

    /*
     * This exception can occur when a string is used instead
     * of a integer on a parameter. e.g. /v0/courses/BUG
     * or when is used an illegal character in a url. e.g. /v0/courses/ç/
     */
    @ExceptionHandler(value = [NumberFormatException::class])
    private fun handleNumberFormatException(
        ex: NumberFormatException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
            "",
            "Bad request",
            400,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    private fun handleIllegalArgumentException(
            ex: NumberFormatException,
            request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
                "",
                "Bad request",
                400,
                ex.localizedMessage,
                request.requestURI
        )
    }

    /*
     * This exception is also global for all endpoints that receive parameters
     * page and limit. It should be thrown when they are invalid.
     */
    @ExceptionHandler(value = [IncorrectParametersException::class])
    private fun handleIncorrectParametersException(
        ex: IncorrectParametersException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
            "",
            "Incorrect Query Parameters",
            400,
            ex.localizedMessage,
            request.requestURI
        )
    }

    /*
     * Occurs when an Unauthenticated user
     * tries to access a restricted resource.
     */
    @ExceptionHandler(value = [UnauthenticatedUserException::class])
    private fun handleUnauthenticatedAccess(
        ex: UnauthenticatedUserException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        val headers = HttpHeaders()
        headers.add("WWW-Authenticate", "Bearer realm=\"I-ON\"")

        return handleResponse(
            "",
            "UNAUTHORIZED",
            401,
            ex.localizedMessage,
            request.requestURI,
            headers
        )
    }

    /*
     * Occurs when an Authenticated User
     * tries to access a resource that has no permissions for.
     */
    @ExceptionHandler(value = [ForbiddenActionException::class])
    private fun handleForbiddenActionException(
      ex: ForbiddenActionException,
      request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleResponse(
          "",
          "FORBIDDEN",
          403,
          ex.localizedMessage,
          request.requestURI
        )
    }

    private fun handleResponse(
        type: String,
        title: String,
        status: Int,
        detail: String,
        instance: String,
        customHeaders: HttpHeaders = HttpHeaders()
    ): ResponseEntity<ProblemJson> {
        customHeaders.add("Content-Type", Media.PROBLEM_JSON)

        return ResponseEntity
            .status(status)
            .headers(customHeaders)
            .body(ProblemJson(type, title, status, detail, instance))
    }

}