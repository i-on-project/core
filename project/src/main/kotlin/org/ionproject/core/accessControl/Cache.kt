package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.ionproject.core.common.interceptors.LoggerInterceptor
import org.ionproject.core.common.transaction.DataSourceHolder
import org.ionproject.core.common.transaction.TransactionManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

/**
 *  Cache used to avoid having to check database everytime
 *  a request is made and needs authentication.
 */
@Component
class AccessControlCache {

    private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))

    private val cacheManager : CacheManager = CaffeineConfiguration().cacheManager()
    private val tokenCache = cacheManager.getCache(CaffeineConfiguration.tokenCache)
    private val policiesCache = cacheManager.getCache(CaffeineConfiguration.policiesCache)

    @Cacheable(cacheNames = [CaffeineConfiguration.tokenCache], key = "#tokenReference")
    fun getToken(tokenReference: String, derived: Boolean) : TokenEntity {

        val cacheValue = tokenCache?.get(tokenReference)
        val token : TokenEntity?

        if(cacheValue == null) {
            logger.info("No cache hit, reading token from database...")
            token = pap.getToken(tokenReference, derived)

            //Check if the token exists
            if (token == null) {
                logger.info(
                    LogMessages.forAuthError(
                        LogMessages.tokenHeaderAuth,
                        LogMessages.inexistentToken
                    )
                )
                throw UnauthenticatedUserException(LogMessages.inexistentTokenMessage)
            }

            tokenCache?.put(tokenReference, token)
            return token
        } else {
            logger.info("Token cache hit!")
            return cacheValue.get() as TokenEntity
        }
    }

    @Cacheable(value = [CaffeineConfiguration.policiesCache], key = "#scope")
    fun getPolicies(scope: String, version: String) : List<PolicyEntity> {

        val cacheValue = policiesCache?.get(scope)
        val policies : List<PolicyEntity>

        if(cacheValue  == null) {
            logger.info("No cache hit, reading policies from database...")

            policies = pap.getPolicies(scope, version)
            policiesCache?.put(scope, policies)
            return policies
        } else {
            logger.info("Policies cache hit!")
            return cacheValue.get() as List<PolicyEntity>
        }

    }
}
