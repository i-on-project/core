package org.ionproject.core.user.common.email

enum class EmailType(val mimeType: String) {
    TEXT("text/plain"),
    HTML("text/html")
}

abstract class EmailService(val senderEmail: String, val senderName: String) {

    abstract suspend fun sendEmail(recipientEmail: String, emailType: EmailType, subject: String, content: String)
}
