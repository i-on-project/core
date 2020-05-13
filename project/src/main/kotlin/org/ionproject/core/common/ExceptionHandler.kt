package org.ionproject.core.common

import org.ionproject.core.common.customExceptions.*
import org.postgresql.util.PSQLException
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

  @ExceptionHandler(value = [PSQLException::class])
  private fun handleUnableToCreateStatementException(
    ex: PSQLException,
    request: HttpServletRequest
  ): ResponseEntity<ProblemJson> {
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
  ): ResponseEntity<ProblemJson> {
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
  @ExceptionHandler(value = [NumberFormatException::class, IllegalArgumentException::class])
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
  private fun handleUnauthenticatedAccess() {
  }

  /*
   * Occurs when an Authenticated User
   * tries to access a resource that has no permissions for.
   */
  @ExceptionHandler(value = [ProhibitedUserException::class])
  private fun handleProhibitedAccess() {
  }

  private fun handleResponse(
    type: String,
    title: String,
    status: Int,
    detail: String,
    instance: String
  ): ResponseEntity<ProblemJson> {
    return ResponseEntity
      .status(status)
      .header("Content-Type", Media.PROBLEM_JSON)
      .body(ProblemJson(type, title, status, detail, instance))
  }

}