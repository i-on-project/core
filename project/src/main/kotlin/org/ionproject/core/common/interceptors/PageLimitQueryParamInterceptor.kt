package org.ionproject.core.common.interceptors

import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
  * Tests query parameters 'page' and 'limit'.
  * Page & Limit can have different meanings according
  * to the endpoint which is requested.
 */

class PageLimitQueryParamInterceptor : HandlerInterceptorAdapter() {
    companion object {
        const val MAX_LIMIT = 100   //It's unlikely to have anything above it but just in case a limit is established.
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val pageValue = request.getParameter("page")
        val limitValue = request.getParameter("limit")

        if(pageValue != null) {            //If its null, user didn't use query params and there is nothing to test
            val page = pageValue.toInt()
            if(page < 0) {
                throw IncorrectParametersException("The parameter page can't be negative, page=$page")
            }
        }

        if(limitValue != null) {
            val limit = limitValue.toInt()
            if(limit < 0 || limit > MAX_LIMIT) {
                throw IncorrectParametersException("The parameter limit can't be negative or above the limit, limit=$limit")
            }
        }

        return true
    }
}