package org.ionproject.core.common.filters

import org.ionproject.core.common.LogMessages
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(2)
class LoggerFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var startTime = 0L
        try {
            startTime = System.currentTimeMillis()


            logger.info(
                LogMessages.forLoggerAccessMessage(
                    request.remoteAddr,
                    request.method,
                    request.requestURI
                )
            )
            filterChain.doFilter(request, response)
        } finally {
            val matchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)?.toString() ?: ""

            val endTime = System.currentTimeMillis()
            logger.info(
                LogMessages.forLoggerCompletionMessage(
                    endTime - startTime,
                    matchingPattern
                )
            )
        }
    }

}