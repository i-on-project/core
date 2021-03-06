package org.ionproject.core.userApi.auth.registry

import com.fasterxml.jackson.annotation.JsonProperty
import org.ionproject.core.common.Uri
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.email.EmailService
import org.ionproject.core.common.email.EmailType
import org.ionproject.core.userApi.auth.model.AuthRequestHelper
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AuthMethodRegistry {

    private val registry = mutableMapOf<String, AuthMethod>()
    val authMethods: Iterable<AuthMethod>
        get() = registry.values

    fun register(method: AuthMethod) {
        registry[method.type] = method
    }

    operator fun get(type: String) = registry[type]
}

abstract class AuthMethod(
    val type: String
) {

    abstract suspend fun solve(request: AuthRequestHelper)
}

data class EmailAuthMethod(
    @JsonProperty("allowed_domains")
    val allowedDomains: List<String>,
    private val emailService: EmailService
) : AuthMethod("email") {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    private val allowedDomainsParts = allowedDomains.map {
        it.split(".")
            .asReversed()
    }

    override suspend fun solve(request: AuthRequestHelper) {
        val email = request.loginHint
        if (!validateEmail(email))
            throw BadRequestException("The domain of the email is not allowed!")

        val zoneId = "UTC"
        val time = timeFormatter.format(
            LocalDateTime.ofInstant(
                request.time,
                ZoneId.of(zoneId)
            )
        )

        val verifyUrl = Uri.forAuthVerifyFrontend(request.authRequestId, request.secretId)
        emailService.sendEmail(
            email,
            EmailType.HTML,
            "ion - Verify your login attempt",
            """
                <h1>New Login Attempt from ${request.client.clientName}</h1>
                <h3>$time $zoneId with ${request.userAgent}</h3>
                <b>If this was not you please ignore and delete this email!</b>
                <br>
                <br>
                <p>To complete your login request please follow the link:</p>
                <p><a href="$verifyUrl">$verifyUrl</a></p>
            """.trimIndent()
        )
    }

    private fun validateEmail(email: String): Boolean {
        val split = email.split("@", limit = 2)
        if (split.size != 2)
            throw BadRequestException("Invalid email!")

        val domainParts = split[1].split(".")
            .asReversed()

        val adp = allowedDomainsParts.filter {
            it.size <= domainParts.size
        }

        for (dp in adp) {
            for (i in dp.indices) {
                val part = dp[i]
                if (i == dp.size - 1) {
                    if (part == "*")
                        return true

                    if (domainParts.size > i && part == domainParts[i])
                        return true
                }

                if (domainParts.size <= i || part != domainParts[i])
                    break
            }
        }

        return false
    }
}
