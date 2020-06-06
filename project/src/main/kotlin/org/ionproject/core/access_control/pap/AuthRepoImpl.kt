package org.ionproject.core.access_control.pap

import org.ionproject.core.access_control.pap.AuthRepoData.GET_TOKEN_QUERY
import org.ionproject.core.access_control.pap.AuthRepoData.TOKEN
import org.ionproject.core.common.transaction.TransactionManager
import org.springframework.stereotype.Repository

@Repository
class AuthRepoImpl(private val tm: TransactionManager) : AuthRepo {
  private val tokenMapper = TokenMapper()

  override fun getTableToken(tokenHash: String): TokenEntity? = tm.run { handle ->
    handle.createQuery(GET_TOKEN_QUERY)
      .bind(TOKEN, tokenHash)
      .map(tokenMapper)
      .firstOrNull()
  }
}