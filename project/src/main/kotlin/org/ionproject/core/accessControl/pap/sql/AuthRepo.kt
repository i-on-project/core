package org.ionproject.core.accessControl.pap.sql

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity

interface AuthRepo {
    fun getTableToken(tokenHash: String): TokenEntity?

    fun getPolicies(scope: String, apiVersion: String): List<PolicyEntity>

    fun checkScopeExistence(scope: String): Boolean

    fun storeToken(token: TokenEntity): Boolean

    fun revokeToken(hash: String): Boolean

    fun revokeChild(hash: String): Boolean

    fun revokePresentedAndChild(hash: String): Boolean

    fun generateImportToken(fatherTokenHash: String): String

    fun getDerivedTableToken(tokenHash: String): TokenEntity?
}
