package org.ionproject.core.accessControl.pap.sql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.API_VERSION
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.GET_POLICIES_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.GET_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.SCOPE_URI
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.TOKEN
import org.ionproject.core.accessControl.pap.entities.PolicyEntity
import org.ionproject.core.accessControl.pap.entities.TokenEntity
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.INSERT_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.REVOKE_TOKEN_QUERY
import org.ionproject.core.readApi.common.transaction.TransactionManager
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

  override fun getPolicies(scope: String, apiVersion: String): List<PolicyEntity> = tm.run { handle ->
    handle.createQuery(GET_POLICIES_QUERY)
      .bind(SCOPE_URI, scope)
      .bind(API_VERSION, apiVersion)
      .map(policyMapper)
      .list()
  } as List<PolicyEntity>

  override fun storeToken(token: TokenEntity) : Boolean = tm.run {
    handle -> {
      val mapper = jacksonObjectMapper()
      val claimsData =  mapper.writeValueAsString(token.claims)

      val result = handle.execute(INSERT_TOKEN_QUERY, token.hash, token.isValid, token.issuedAt, token.expiresAt, claimsData)
      result > 0
    }()
  }

  override fun revokeToken(hash: String): Boolean = tm.run {
    handle -> {
      val result = handle.execute(REVOKE_TOKEN_QUERY, hash)
      result > 0
    } ()
  }
}