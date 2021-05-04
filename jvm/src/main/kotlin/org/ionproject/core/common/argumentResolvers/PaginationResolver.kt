package org.ionproject.core.common.argumentResolvers

import org.ionproject.core.common.argumentResolvers.parameters.Pagination
import org.ionproject.core.common.customExceptions.IncorrectParametersException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val PAGE_PARAMETER_NAME = "page"
private const val LIMIT_PARAMETER_NAME = "limit"
private const val DEFAULT_PAGE = 0
private const val DEFAULT_LIMIT = 10
private const val MAX_LIMIT = 100

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PaginationDefaults(val defaultPage: Int = DEFAULT_PAGE, val defaultLimit: Int = DEFAULT_PAGE)

class PaginationResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == Pagination::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Pagination {
        val pageStr = webRequest.getParameter(PAGE_PARAMETER_NAME)
        val limitStr = webRequest.getParameter(LIMIT_PARAMETER_NAME)

        val defaults = parameter.getParameterAnnotation(PaginationDefaults::class.java)
        var page = defaults?.defaultPage ?: DEFAULT_PAGE
        var limit = defaults?.defaultLimit ?: DEFAULT_LIMIT

        if (pageStr != null) {
            page = pageStr.toInt()
            if (page < 0) {
                throw IncorrectParametersException("The parameter page can't be negative, page=$page")
            }
        }

        if (limitStr != null) {
            limit = limitStr.toInt()
            if (limit < 0 || limit > MAX_LIMIT) {
                throw IncorrectParametersException("The parameter limit can't be negative or above the limit, limit=$limit")
            }
        }

        return Pagination(page, limit)
    }
}
