package org.ionproject.core.access_control.pap

interface AuthRepo {
  fun getTableToken(tokenHash: String): TokenEntity?

  fun getPolicies(scope: String, apiVersion: String): List<Policy>
}