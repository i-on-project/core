package org.ionproject.core.user.auth

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.runBlocking
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.user.common.email.EmailService
import org.ionproject.core.user.common.email.EmailType

class AuthMethodRegistry {

    private val registry = mutableMapOf<String, AuthMethod>()
    val authMethods: Iterable<AuthMethod>
        get() = registry.values

    fun register(method: AuthMethod) {
        registry[method.type] = method
    }

    operator fun get(type: String) =
        registry[type] ?: throw BadRequestException("Invalid auth method type!")

}

abstract class AuthMethod(val type: String) {

    // TODO: the return should be changed
    abstract fun solve(data: String?): Boolean

}

data class EmailAuthMethod(
    @JsonProperty("allowed_domains")
    val allowedDomains: List<String>,
    private val emailService: EmailService
): AuthMethod("email") {

    private val allowedDomainsParts = allowedDomains.map {
        it.split(".")
            .asReversed()
    }

    override fun solve(data: String?): Boolean {
        // TODO: finish this
        data ?: throw BadRequestException("The email auth method requires an email to be provided!")

        if (!validateEmail(data))
            throw BadRequestException("The domain of the email is not allowed!")

        runBlocking {
            emailService.sendEmail(
                data,
                EmailType.TEXT,
                "Test Email",
                "Yay! We made it boys!"
            )
        }

        return true
    }

    private fun validateEmail(email: String): Boolean {
        val split = email.split("@", limit = 2)
        if (split.size != 2)
            throw BadRequestException("Invalid email!")

        val domainParts = split[1].split(".")
            .asReversed()

        val adp = allowedDomainsParts.filter {
            it.size == domainParts.size || it.size - 1 == domainParts.size
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