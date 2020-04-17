package org.ionproject.core.common

import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.slf4j.LoggerFactory

/**
 * For now its output is the console, probably should be changed to
 * a log file and add it .gitignore
 *
 * Also, stored IP's should be temporary and only stored for a couple days deleting when
 * no longer needed to provide security.
 * With exception to some malicious blacklisted ip's.
 * As required by the GDPR.
 */
object Logger {
    private val log = LoggerFactory.getLogger(LoggerInterceptor::class.java)

    fun logInfo(message: String) {
        log.info("[INFO]: $message")
    }

    fun logWarning(message: String) {
        log.warn("[WARNING]: $message")
    }

    fun logError(message: String) {
        log.error("[ERROR]: $message")
    }
}
