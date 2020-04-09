package org.ionproject.core.common

import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.ionproject.core.common.customExceptions.ProhibitedUserException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.NotAcceptableStatusException
import org.springframework.web.servlet.NoHandlerFoundException
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.NumberFormatException
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
    /*
     * This exception can occur when a string is used instead
     * of a integer on a parameter. e.g. /v0/courses/BUG
     * or when is used an illegal character in a url. e.g. /v0/courses/ç/
     */
    @ExceptionHandler(value = [NumberFormatException::class,IllegalArgumentException::class])
    private fun handleNumberFormatException(ex: NumberFormatException, request : HttpServletRequest) : ResponseEntity<*> {
        return handleResponse("https://en.wikipedia.org/wiki/List_of_HTTP_status_codes",
                "Bad request",
                400,
                "Incorrect values passed to parameters. (Check Read API)",
                request.requestURI
        )
    }

    /*
     * This exception is also global for all endpoints that receive parameters
     * page and limit. It should be thrown when they are invalid.
     */
    @ExceptionHandler(value = [IncorrectParametersException::class])
    private fun handleIncorrectParametersException(ex : IncorrectParametersException, request: HttpServletRequest) : ResponseEntity<*> {
        return handleResponse(
                "https://pt.wikipedia.org/wiki/HTTP_400",
                "Incorrect Query Parameters",
                400,
                "The query parameters values passed are not correct (negative numbers).",
                "${request.requestURI}${ex.message}"
        )
    }
    /*
     * Occurs when an Unauthenticated user
     * tries to access a restricted resource.
     */
    @ExceptionHandler(value = [UnauthenticatedUserException::class])
    private fun handleUnauthenticatedAccess() {}

    /*
     * Occurs when an Authenticated User
     * tries to access a resource that has no permissions for.
     */
    @ExceptionHandler(value = [ProhibitedUserException::class])
    private fun handleProhibitedAccess(){}

    private fun handleResponse(type: String, title: String, status: Int, detail: String, instance: String) : ResponseEntity<*> {
        return ResponseEntity
                .status(status)
                .header("Content-Type",Media.PROBLEM_JSON.toString())
                .body(ProblemJson(type, title, status, detail, instance))
    }

}