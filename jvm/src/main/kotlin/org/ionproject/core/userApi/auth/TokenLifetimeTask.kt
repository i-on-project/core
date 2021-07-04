package org.ionproject.core.userApi.auth

import org.ionproject.core.userApi.auth.repo.UserAuthRepo
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TokenLifetimeTask(val repo: UserAuthRepo) {

    companion object {
        private val log = LoggerFactory.getLogger(TokenLifetimeTask::class.java)
    }

    @Scheduled(cron = "0 0 0 * * 0")
    fun revokeOlderTokens() {
        log.info("Revoking older access tokens")
        repo.revokeOlderTokens()
    }
}
