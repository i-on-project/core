package org.ionproject.core.user.common.email

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.ionproject.core.common.customExceptions.InternalServerErrorException
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val SEND_GRID_ENDPOINT = "https://api.sendgrid.com/v3/mail/send"

private val logger = LoggerFactory.getLogger(SendGridEmailService::class.java)

class SendGridEmailService(
    private val httpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    private val apiKey: String,
    senderEmail: String,
    senderName: String
) : EmailService(senderEmail, senderName) {

    override suspend fun sendEmail(
        recipientEmail: String,
        emailType: EmailType,
        subject: String,
        content: String
    ) {
        val request = buildRequest(recipientEmail, emailType, subject, content)
        return suspendCoroutine {
            httpClient.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        logger.error(e.localizedMessage)
                        it.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            val errorMessage = "Unexpected error while sending the email"
                            val realErrorMessage = objectMapper.readValue(
                                response.body?.string(),
                                EmailError::class.java
                            ).errors?.get(0)?.message ?: errorMessage

                            logger.error(realErrorMessage)
                            it.resumeWithException(InternalServerErrorException(errorMessage))
                        } else {
                            it.resume(Unit)
                        }
                    }
                })
        }
    }

    // API documentation available at https://sendgrid.com/docs/api-reference/
    private fun buildRequest(
        recipientEmail: String,
        emailType: EmailType,
        subject: String,
        content: String
    ): Request {
        val emailRequestBody = toEmailRequestBody(recipientEmail, emailType, subject, content)
        val jsonRequestBody = objectMapper.writeValueAsBytes(emailRequestBody)

        return Request.Builder()
            .url(SEND_GRID_ENDPOINT)
            .post(jsonRequestBody.toRequestBody("application/json".toMediaType()))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
    }

    private fun toEmailRequestBody(
        recipientEmail: String,
        emailType: EmailType,
        subject: String,
        content: String
    ) = EmailRequestBody(
        listOf(
            EmailPersonalization(
                listOf(Email(recipientEmail))
            )
        ),
        Email(senderEmail, senderName),
        subject,
        listOf(EmailContent(emailType.mimeType, content))
    )

    data class EmailError(
        val errors: List<EmailErrorDetail>? = null,
        val id: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EmailErrorDetail(
        val message: String? = null,
        val field: String? = null
    )

    data class EmailRequestBody(
        val personalizations: List<EmailPersonalization>,
        val from: Email,
        val subject: String,
        val content: List<EmailContent>
    )

    data class EmailPersonalization(
        val to: List<Email>
    )

    data class EmailContent(
        val type: String,
        val value: String
    )

    data class Email(
        val email: String,
        val name: String? = null
    )
}
