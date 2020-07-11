package org.ionproject.core.accessControl

import com.github.benmanes.caffeine.cache.Caffeine
import org.checkerframework.checker.nullness.qual.NonNull
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CaffeineConfiguration {

    companion object {
         const val expireTime: Long = 60
         const val tokenCache = "tokens"
         const val policiesCache = "policies"
    }

    @Bean
    fun cacheManager(): CacheManager {
        val tokensCache = buildCache()

        val cacheManager = CaffeineCacheManager(tokenCache, policiesCache)
        cacheManager.setCaffeine(tokensCache)

        return cacheManager
    }

    private fun buildCache() : Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .expireAfterWrite(expireTime, TimeUnit.SECONDS)

    }
}