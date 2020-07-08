package org.ionproject.core.error

import org.ionproject.core.common.ProblemJson
import org.ionproject.core.common.ResourceIdentifierAnnotation
import org.ionproject.core.common.ResourceIds
import org.ionproject.core.common.Uri
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@RestController
class MyErrorController : ErrorController {

    private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

    /**
     * Last frontier to catch all error that we miss.
     */
    @RequestMapping(Uri.error)
    @ResourceIdentifierAnnotation(ResourceIds.ERROR, ResourceIds.ALL_VERSIONS)
    fun handleError(request : HttpServletRequest) : ResponseEntity<ProblemJson> {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString()
        val errorMessage = request.getAttribute((RequestDispatcher.ERROR_MESSAGE))

        logger.error("AN ERROR OCURRED | STATUS CODE:$status | REQUEST URI:$requestUri")
        logger.error("ERROR INFO: $errorMessage")

        if(status != null) {
            val statusCode = Integer.valueOf(status.toString())

            when(statusCode) {
                500 -> {
                    return ResponseEntity(ProblemJson("/err", "Error", 500,
                        "Internal Server Error, something on our side happened, try again later...", requestUri), HttpStatus.INTERNAL_SERVER_ERROR)
                }
                406 -> {
                    return ResponseEntity(ProblemJson("/err", "Error", 406,
                        "Sorry, we don't support that mediatype.", requestUri), HttpStatus.NOT_ACCEPTABLE)
                }
                404 -> {
                    return ResponseEntity(ProblemJson("/err", "Error", 404,
                        "Sorry, that endpoint was not found.", requestUri), HttpStatus.NOT_FOUND)
                }
            }
        }

        return ResponseEntity(ProblemJson("/err", "Error", 500,
            "Internal Server Error, something on our side happened, try again later...", requestUri), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun getErrorPath(): String {
        return Uri.error
    }

}