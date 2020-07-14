package org.ionproject.core.accessControl

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepoImpl
import org.ionproject.core.common.LogMessages
import org.ionproject.core.common.customExceptions.BadRequestException
import org.ionproject.core.common.customExceptions.UnauthenticatedUserException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 *  Cache used to avoid having to check database everytime
 *  a request is made and needs authentication.
 */
@Component
class AccessControlCache(private val pap: AuthRepoImpl, cacheConfig: CaffeineConfiguration) {

    private val tokenCache = cacheConfig.tokenCache
    private val policiesCache = cacheConfig.policiesCache

    companion object {
        private val logger = LoggerFactory.getLogger(AccessControlCache::class.java)
    }

    /**
     * saves the requested token by the hash for future
     * requests.
     */
    fun getToken(tokenHash: String, derived: Boolean) : TokenEntity {
        return tokenCache.get(tokenHash) { readTokenDb(tokenHash, derived) }
            ?: throw BadRequestException("An error occurred during cache read, you're not supposed to get this message if you do contact the dev team...")
    }

    fun readTokenDb(tokenHash: String, derived: Boolean) : TokenEntity {
        val token = pap.getToken(tokenHash, derived)

        //Check if the token exists
        if (token == null) {
            logger.info(
                LogMessages.forAuthError(
                    LogMessages.inexistentToken
                )
            )
            throw UnauthenticatedUserException(LogMessages.inexistentTokenMessage)
        }

        return token
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

        return policiesCache.get(id) { pap.getPolicies(scope, version) }
            ?: throw BadRequestException("An error occurred during cache read, you're not supposed to get this message if you do contact the dev team...")
    }

    /**
     * Used to clear the cache,
     * invalidate has immediate results opposed to clear()
     * useful during the tests where its needed to issue a token and revoke it after,
     * if the cache wasn't cleared the test would fail.
     */
    fun clearCache() {
        policiesCache.invalidateAll()
        tokenCache.invalidateAll()
    }
}
