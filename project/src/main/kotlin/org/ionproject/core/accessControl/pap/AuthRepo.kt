package org.ionproject.core.accessControl.pap

interface AuthRepo {
  fun getTableToken(tokenHash: String): TokenEntity?

  fun getPolicies(scope: String, apiVersion: String): List<Policy>
}