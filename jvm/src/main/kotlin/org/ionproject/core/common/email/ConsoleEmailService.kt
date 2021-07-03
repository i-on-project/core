package org.ionproject.core.common.email

import org.slf4j.LoggerFactory

class ConsoleEmailService : EmailService("", "") {

    companion object {
        private val logger = LoggerFactory.getLogger(ConsoleEmailService::class.java)
        private val htmlTagPattern = "<.*?>".toRegex()
    }

    override suspend fun sendEmail(
        recipientEmail: String,
        emailType: EmailType,
        subject: String,
        content: String
    ) {
        var textContent = content
        if (emailType == EmailType.HTML)
            textContent = content.replace(htmlTagPattern, "")

        logger.info(
            """
                
---------------------------------------------------------
An email has been sent
Recipient: $recipientEmail
Subject: $subject
Content-Type: ${emailType.mimeType}
                
Content: 
                
$textContent
                
---------------------------------------------------------
            """.trimIndent()
        )
    }
}
