package org.ionproject.core.common.interceptors

import org.ionproject.core.common.Logger
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class LoggerInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val startTime = System.currentTimeMillis()
        Logger.logInfo(
                "IP:${request.remoteAddr} | Method:${request.method} | Endpoint:${request.requestURI} | Timestamp:${startTime}"
        )
        request.setAttribute("startTime", startTime)
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        val startTime : Long = request.getAttribute("startTime") as Long
        val endTime : Long = System.currentTimeMillis()
        Logger.logInfo(
                """
                    Total time taken to proccess request in milliseconds: ${endTime-startTime} ms
                """.trimIndent()
        )
    }
}