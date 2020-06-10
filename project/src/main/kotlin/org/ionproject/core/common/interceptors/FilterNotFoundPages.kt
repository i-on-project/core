package org.ionproject.core.common.interceptors

import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Filters not found pages to avoid passing down to other interceptors
 * and waste processing time
 *
 * checking handler method name doesnt work
 */
class FilterNotFoundPages : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(request.requestURI == "/error")
            return false

        return true
    }
}