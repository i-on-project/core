package org.ionproject.core.user.common.email

import org.slf4j.LoggerFactory

class MockEmailService : EmailService("", "") {

    companion object {
        private val logger = LoggerFactory.getLogger(MockEmailService::class.java)
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
