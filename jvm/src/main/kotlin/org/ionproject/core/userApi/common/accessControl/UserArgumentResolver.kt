package org.ionproject.core.userApi.common.accessControl

import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.ionproject.core.userApi.user.model.User
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(val repo: UserAuthRepo) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == User::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ) = webRequest.getAttribute(UserAccessInterceptor.USER_INFO, RequestAttributes.SCOPE_REQUEST) as User?
}
