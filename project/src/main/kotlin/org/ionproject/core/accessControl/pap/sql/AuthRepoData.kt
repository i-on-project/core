package org.ionproject.core.accessControl.pap.sql

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

    const val HASH = "hash"
    const val IS_VALID = "isValid"
    const val ISSUED_AT = "issuedAt"
    const val EXPIRES_AT = "expiresAt"
    const val CLAIMS = "claims"
    const val IS_DERIVED_TOKEN = "derivedToken"
    const val FATHER_TOKEN_HASH = "fatherHash"
    const val CALENDAR_READ_SCOPE = "urn:org:ionproject:scopes:api:read:calendar"

    const val GET_TOKEN_QUERY = "SELECT * FROM $SCHEMA.$TOKEN WHERE $HASH_ID=:$TOKEN"

    const val GET_SCOPE = "SELECT COUNT(*) FROM $SCHEMA.$SCOPES WHERE $SCOPE_URI=:$SCOPE_URI"

    const val GET_POLICIES_QUERY = """
      SELECT * FROM $SCHEMA.$POLICIES 
      WHERE $API_VERSION=:$API_VERSION 
        AND $SCOPE_ID=(SELECT $ID FROM $SCHEMA.$SCOPES WHERE $SCOPE_URI=:$SCOPE_URI)
  """

    const val INSERT_TOKEN_QUERY = """
    INSERT INTO $SCHEMA.$TOKEN ($HASH,$IS_VALID,$ISSUED_AT,$EXPIRES_AT,$IS_DERIVED_TOKEN,$FATHER_TOKEN_HASH,$CLAIMS) VALUES (?,?,?,?,?,?,to_json(?::json))
  """

    const val REVOKE_TOKEN_QUERY = """
    UPDATE $SCHEMA.$TOKEN SET $IS_VALID=false WHERE $HASH=?
  """

    const val REVOKE_CHILD_TOKEN_QUERY = """
       UPDATE $SCHEMA.$TOKEN SET $IS_VALID=false WHERE $FATHER_TOKEN_HASH=? 
    """

    const val GET_IMPORT_TOKEN = """
        SELECT * FROM $SCHEMA.$TOKEN 
        WHERE $IS_DERIVED_TOKEN=TRUE 
        AND $IS_VALID=TRUE 
        AND $FATHER_TOKEN_HASH=:$FATHER_TOKEN_HASH 
        AND $CLAIMS ->> '$SCOPE_URI'=:$SCOPE_URI
    """
}