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
import org.springframework.stereotype.Component

/**
 *  Cache used to avoid having to check database everytime
 *  a request is made and needs authentication.
 */
@Component
class AccessControlCache {

    private val logger = LoggerFactory.getLogger(LoggerInterceptor::class.java)
    private val pap: AuthRepoImpl = AuthRepoImpl(TransactionManagerImpl(DataSourceHolder))

    companion object {
        private val cacheManager: CacheManager = CaffeineConfiguration().cacheManager()
        private val tokenCache = cacheManager.getCache(CaffeineConfiguration.tokenCache)
        private val policiesCache = cacheManager.getCache(CaffeineConfiguration.policiesCache)
    }

    /**
     * saves the requested token by the hash for future
     * requests.
     */
    fun getToken(tokenHash: String, derived: Boolean) : TokenEntity {

        val cacheValue = tokenCache?.get(tokenHash)
        val token : TokenEntity?

        if(cacheValue == null) {
            logger.info("No cache hit, reading token from database...")
            token = pap.getToken(tokenHash, derived)

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

            tokenCache?.put(tokenHash, token)
            return token
        } else {
            logger.info("Token cache hit!")
            return cacheValue.get() as TokenEntity
        }
    }


    /**
     * gets the policies out of the cash if they exist
     * or reads them from the database.
     *
     * the id to store and read from cache is made of the scope and version
     * of the request resource, the reason for that is that a scope
     * can have permissions for multiple objects with different versions,
     * if the only condition was to save by scope, it would many times
     * occur a 403 Forbidden because of the wrong policies for the incorrect version.
     *
     */
    fun getPolicies(scope: String, version: String) : List<PolicyEntity> {
        val id = "$scope.$version"
        val cacheValue = policiesCache?.get(id)
        val policies : List<PolicyEntity>

        if(cacheValue  == null) {
            logger.info("No cache hit, reading policies from database...")

            policies = pap.getPolicies(scope, version)
            policiesCache?.put(id, policies)
            return policies
        } else {
            logger.info("Policies cache hit!")
            return cacheValue.get() as List<PolicyEntity>
        }

    }

    /**
     * Used to clear the cache,
     * invalidate has immediate results opposed to clear()
     * useful during the tests where its needed to issue a token and revoke it after,
     * if the cache wasn't cleared the test would fail.
     */
    fun clearCache() {
        policiesCache?.invalidate()
        tokenCache?.invalidate()
    }
}
