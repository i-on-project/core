package org.ionproject.core.accessControl

import com.github.benmanes.caffeine.cache.Caffeine
import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
object CaffeineConfiguration {

    private const val expireTime: Long = 60

    val tokenCache = Caffeine.newBuilder()
        .expireAfterWrite(expireTime, TimeUnit.SECONDS)
        .build<String, TokenEntity>()

    val policiesCache = Caffeine.newBuilder()
        .expireAfterWrite(expireTime, TimeUnit.SECONDS)
        .build<String, List<PolicyEntity>>()
}
