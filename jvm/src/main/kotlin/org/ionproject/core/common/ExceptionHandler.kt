package org.ionproject.core.common

import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.ForbiddenActionException
import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.ionproject.core.common.customExceptions.ResourceNotFoundException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.format.DateTimeParseException
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

private val logger = LoggerFactory.getLogger("ExceptionHandler")

fun handleExceptionResponse(
    type: String,
    title: String,
    status: Int,
    detail: String,
    instance: String,
    customHeaders: HttpHeaders = HttpHeaders()
): ResponseEntity<ProblemJson> {
    customHeaders.add("Content-Type", Media.PROBLEM_JSON)

    logger.error(
        LogMessages.forException(
            instance,
            detail
        )
    )

    return ResponseEntity
        .status(status)
        .headers(customHeaders)
        .body(ProblemJson(type, title, status, detail, instance))
}

@RestControllerAdvice
class ExceptionHandler(val resourceLoader: ResourceLoader) {

    @ExceptionHandler(value = [BadRequestException::class])
    private fun handleBadRequestException(
        ex: BadRequestException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/bad-request",
            "Bad request",
            400,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [DateTimeParseException::class])
    private fun handleDateTimeParseExceptionException(
        ex: DateTimeParseException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "https://github.com/i-on-project/core/tree/master/docs/api/events.md#invalid-date-type",
            "Invalid Date format",
            400,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [PSQLException::class])
    private fun handleUnableToCreateStatementException(
        ex: PSQLException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/internal-db-error",
            "Internal Error (DB)",
            500,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    private fun handleHttpRequestMethodNotSupportedException(
        ex: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/method-not-allowed",
            "Method not allowed for the target resource",
            405,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [HttpMediaTypeNotSupportedException::class])
    private fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/media-type-not-supported",
            "This resource does not support the provided media type",
            415,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    private fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/message-not-readable",
            "Could not interpret the contents of the message body.",
            400,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    private fun handleInternalServerErrorException(
        ex: InternalServerErrorException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemJson> {
        return handleExceptionResponse(
            "/err/internal-error",
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
        return handleExceptionResponse(
            "/err/resource-not-found",
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
        return handleExceptionResponse(
            "/err/bad-request",
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
        return handleExceptionResponse(
            "/err/bad-request",
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
        return handleExceptionResponse(
            "/err/incorrect-params",
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

        return handleExceptionResponse(
            "/err/unauthorized",
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
        return handleExceptionResponse(
            "/err/forbidden",
            "FORBIDDEN",
            403,
            ex.localizedMessage,
            request.requestURI
        )
    }
}
