package org.ionproject.core.common.filters

import org.slf4j.MDC // if we use logback switch the import. The method name is the same so everything should work accordingly
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val REQUEST_ID_HEADER = "Request-Id"
const val REQUEST_ID = "req-id"

@Component
@Order(1)
class RequestIDFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()

        MDC.put(REQUEST_ID, requestId)

        response.addHeader(
            REQUEST_ID_HEADER,
            requestId
        )

        filterChain.doFilter(request, response)
    }
}
