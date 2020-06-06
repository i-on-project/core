package org.ionproject.core.access_control.pap

internal object AuthRepoData {
  const val SCHEMA = "dbo"
  const val TOKEN = "Token"
  const val POLICIES = "policies"

  const val HASH_ID = "hash"

  const val GET_TOKEN_QUERY = "SELECT * FROM $SCHEMA.$TOKEN WHERE $HASH_ID=:$TOKEN"
}