package org.ionproject.core.common.interceptors

import org.ionproject.core.common.LogMessages
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

class LoggerInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val startTime = System.currentTimeMillis()
        logger.info(
            LogMessages.forLoggerAccessMessage(
                request.remoteAddr,
                request.method,
                request.requestURI,
                startTime
            )
        )
        request.setAttribute("startTime", startTime)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val startTime: Long = request.getAttribute("startTime") as Long
        val endTime: Long = System.currentTimeMillis()
        logger.info(
            LogMessages.forLoggerCompletionMessage(
                startTime,
                endTime
            )
        )
    }
}