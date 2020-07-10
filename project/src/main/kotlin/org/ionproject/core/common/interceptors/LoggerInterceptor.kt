package org.ionproject.core.common.interceptors

import org.ionproject.core.common.filters.REQUEST_ID
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)

class LoggerInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val startTime = System.currentTimeMillis()
        logger.info(
            "IP:${request.remoteAddr} | Method:${request.method} | Endpoint:${request.requestURI} | Request-Id:${MDC.get(
                REQUEST_ID)} | Timestamp:${startTime}"
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
            """
                    Request-Id:${MDC.get(REQUEST_ID)} | Total time taken to proccess request in milliseconds: ${endTime - startTime} ms
                """.trimIndent()
        )
    }
}