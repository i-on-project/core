package org.ionproject.core.accessControl.pap

internal object AuthRepoData {
  const val SCHEMA = "dbo"
  const val TOKEN = "Token"
  const val HASH_ID = "hash"

  const val POLICIES = "policies"
  const val SCOPE_ID = "scope_id"
  const val SCOPE_URI = "scope"
  const val API_VERSION = "version"
  const val SCOPES = "scopes"
  const val ID = "id"


  const val GET_TOKEN_QUERY = "SELECT * FROM $SCHEMA.$TOKEN WHERE $HASH_ID=:$TOKEN"

  const val GET_POLICIES_QUERY = """
      SELECT * FROM $SCHEMA.$POLICIES 
      WHERE $API_VERSION=:$API_VERSION 
        AND $SCOPE_ID=(SELECT $ID FROM $SCHEMA.$SCOPES WHERE $SCOPE_URI=:$SCOPE_URI)
  """
}