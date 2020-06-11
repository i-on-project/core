package org.ionproject.core.accessControl.pap.sql

import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity

interface AuthRepo {
  fun getTableToken(tokenHash: String): TokenEntity?

  fun getPolicies(scope: String, apiVersion: String): List<PolicyEntity>

  fun storeToken(token: TokenEntity): Boolean

  fun revokeToken(hash: String): Boolean
}
