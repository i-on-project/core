package org.ionproject.core.accessControl.pap

import org.ionproject.core.accessControl.pap.AuthRepoData.API_VERSION
import org.ionproject.core.accessControl.pap.AuthRepoData.GET_POLICIES_QUERY
import org.ionproject.core.accessControl.pap.AuthRepoData.GET_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.AuthRepoData.SCOPE_URI
import org.ionproject.core.accessControl.pap.AuthRepoData.TOKEN
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

@Repository
class AuthRepoImpl(private val tm: TransactionManager) : AuthRepo {
  private val tokenMapper = TokenMapper()
  private val policyMapper = PolicyMapper()

  override fun getTableToken(tokenHash: String): TokenEntity? = tm.run { handle ->
    handle.createQuery(GET_TOKEN_QUERY)
      .bind(TOKEN, tokenHash)
      .map(tokenMapper)
      .firstOrNull()
  }

  override fun getPolicies(scope: String, apiVersion: String): List<Policy> = tm.run {handle ->
    handle.createQuery(GET_POLICIES_QUERY)
      .bind(SCOPE_URI, scope)
      .bind(API_VERSION, apiVersion)
      .map(policyMapper)
      .list()
  } as List<Policy>
}