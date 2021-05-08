package org.ionproject.core.user.common.sql

object UserData {

    const val USER_ID = "userId"
    const val EMAIL = "email"
    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val CLIENT_ID = "clientId"
    const val AT_EXPIRES = "accessTokenExpires"
    const val ID = "id"
    const val SCOPE_ID = "scopeId"

    const val GET_USER_BY_EMAIL = """
        select * from dbo.UserAccount where email = :$EMAIL
    """

    const val GET_USER_BY_ID = """
        select * from dbo.UserAccount where user_id = :$USER_ID
    """

    const val INSERT_USER = """
        insert into dbo.UserAccount (user_id, email)
        values
        (:$USER_ID, :$EMAIL)
    """

    const val INSERT_USER_TOKEN = """
        insert into dbo.UserAccountToken 
        (access_token, refresh_token, user_id, client_id, at_expires)
        values
        (:$ACCESS_TOKEN, :$REFRESH_TOKEN, :$USER_ID, :$CLIENT_ID, :$AT_EXPIRES)
    """

    const val INSERT_USER_TOKEN_SCOPE = """
        insert into dbo.UserAccountTokenScope (id, scope_id)
        values
        (:$ID, :$SCOPE_ID)
    """

    const val GET_USER_TOKEN_BY_CLIENT = """
        select * from dbo.UserAccountToken
        where client_id = :$CLIENT_ID and user_id = :$USER_ID
    """

    const val GET_USER_TOKEN = """
        select * from dbo.UserAccountToken
        where access_token = :$ACCESS_TOKEN and refresh_token = :$REFRESH_TOKEN
    """

    const val REVOKE_USER_TOKEN = """
        delete from dbo.UserAccountToken
        where id = :$ID
    """

    const val REFRESH_USER_TOKEN = """
        update dbo.UserAccountToken
        set 
        access_token = :$ACCESS_TOKEN and 
        refresh_token = :$REFRESH_TOKEN and 
        at_expires = :$AT_EXPIRES and
        updated_at = now()
        where id = :$ID
    """
}
