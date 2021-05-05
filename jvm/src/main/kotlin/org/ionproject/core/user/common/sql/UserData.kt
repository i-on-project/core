package org.ionproject.core.user.common.sql

object UserData {

    const val USER_ID = "userId"
    const val EMAIL = "email"
    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val ID_TOKEN = "idToken"
    const val CLIENT_ID = "clientId"
    const val AT_EXPIRES = "accessTokenExpires"
    const val ID = "id"
    const val SCOPE_ID = "scopeId"

    const val GET_USER_BY_EMAIL = """
        select * from dbo.UserAccount where email = :$EMAIL
    """

    const val INSERT_USER = """
        insert into dbo.UserAccount (user_id, email)
        values
        (:$USER_ID, :$EMAIL)
    """

    const val INSERT_USER_TOKEN = """
        insert into dbo.UserAccountToken 
        (access_token, refresh_token, id_token, user_id, client_id, at_expires)
        values
        (:$ACCESS_TOKEN, :$REFRESH_TOKEN, :$ID_TOKEN, :$USER_ID, :$CLIENT_ID, :$AT_EXPIRES)
    """

    const val INSERT_USER_TOKEN_SCOPE = """
        insert into dbo.UserAccountTokenScope (id, scope_id)
        values
        (:$ID, :$SCOPE_ID)
    """
}
