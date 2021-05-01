package org.ionproject.core.user.auth

import kotlinx.coroutines.runBlocking
import org.ionproject.core.common.transaction.TransactionManager
import org.ionproject.core.user.auth.model.AuthRequest
import org.ionproject.core.user.auth.sql.AuthData
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository

@Repository
class UserAuthRepoImpl(val tm: TransactionManager) : UserAuthRepo {

    override fun addAuthRequest(authRequest: AuthRequest, inTransaction: suspend () -> Unit) =
        tm.run(TransactionIsolationLevel.SERIALIZABLE) {
            val hasClientId = it.createQuery(AuthData.GET_CLIENT_BY_ID)
                .bind(AuthData.CLIENT_ID, authRequest.clientId)
                .mapToMap()
                .findOne()
                .isPresent

            if (!hasClientId)
                throw InvalidClientIdException(authRequest.clientId)

            val hasNotificationMethod = it.createQuery(AuthData.GET_NOTIFICATION_METHOD)
                .bind(AuthData.NOTIFICATION_METHOD, authRequest.notificationMethod)
                .mapToMap()
                .findOne()
                .isPresent

            if (!hasNotificationMethod)
                throw InvalidNotificationMethodException(authRequest.notificationMethod)

            it.createUpdate(AuthData.INSERT_AUTH_REQUEST)
                .bind(AuthData.AUTH_REQUEST_ID, authRequest.authRequestId)
                .bind(AuthData.LOGIN_HINT, authRequest.loginHint)
                .bind(AuthData.USER_AGENT, authRequest.userAgent)
                .bind(AuthData.CLIENT_ID, authRequest.clientId)
                .bind(AuthData.NOTIFICATION_METHOD, authRequest.notificationMethod)
                .bind(AuthData.EXPIRES_ON, authRequest.expiration)
                .execute()

            runBlocking { inTransaction() }
            authRequest
        }

}