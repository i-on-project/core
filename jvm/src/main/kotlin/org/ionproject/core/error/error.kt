package org.ionproject.core.error

import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.ProblemJson
import org.ionproject.core.common.Uri
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.nio.charset.StandardCharsets
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@RestController
class MyErrorController(val resourceLoader: ResourceLoader) : ErrorController {

    @Value("\${react.index-file}")
    lateinit var reactIndex: String

    companion object {
        private val logger = LoggerFactory.getLogger(MyErrorController::class.java)
    }

    /**
     * Last frontier to catch all error that we miss.
     */
    @RequestMapping(Uri.error)
    fun handleError(request: HttpServletRequest): ResponseEntity<Any> {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        val requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString()

        if (status != null) {
            when (Integer.valueOf(status.toString())) {
                406 -> {
                    logger.error(
                        LogMessages.forException(
                            requestUri,
                            LogMessages.unsupportedMediaType
                        )
                    )

                    return ResponseEntity(
                        ProblemJson(
                            "/err",
                            "Error",
                            406,
                            LogMessages.unsupportedMediaType,
                            requestUri
                        ),
                        HttpStatus.NOT_ACCEPTABLE
                    )
                }
                404 -> {
                    val split = requestUri.split("/", limit = 3)
                    if (split.size > 1 && split[1] == "api")
                        return getNotFoundProblem(requestUri)

                    try {
                        resourceLoader.getResource(reactIndex)
                            .inputStream
                            .use {
                                val body = StreamUtils.copyToString(it, StandardCharsets.UTF_8)
                                return ResponseEntity.ok()
                                    .contentType(MediaType.TEXT_HTML)
                                    .body(body)
                            }
                    } catch (ex: Exception) {
                        return getNotFoundProblem(requestUri)
                    }
                }
                500 -> {
                    logger.error(
                        LogMessages.forException(
                            requestUri,
                            LogMessages.internalServerError
                        )
                    )

                    return ResponseEntity(
                        ProblemJson(
                            "/err",
                            "Error",
                            500,
                            LogMessages.internalServerError,
                            requestUri
                        ),
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                }
            }
        }

        logger.error(
            LogMessages.forException(
                requestUri,
                LogMessages.unknownError
            )
        )

        return ResponseEntity(
            ProblemJson(
                "/err",
                "Error",
                500,
                LogMessages.unknownError,
                requestUri
            ),
            HttpStatus.NOT_ACCEPTABLE
        )
    }

    override fun getErrorPath(): String {
        return Uri.error
    }

    private fun getNotFoundProblem(requestUri: String): ResponseEntity<Any> {
        logger.error(
            LogMessages.forException(
                requestUri,
                LogMessages.notFoundResource
            )
        )

        return ResponseEntity(
            ProblemJson(
                "/err",
                "Error",
                404,
                "Sorry, that endpoint was not found.",
                requestUri
            ),
            HttpStatus.NOT_FOUND
        )
    }
}
