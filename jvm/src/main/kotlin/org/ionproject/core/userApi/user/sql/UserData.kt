package org.ionproject.core.userApi.user.sql

object UserData {

    const val TOKEN_ID = "tokenId"
    const val USER_ID = "userId"
    const val EMAIL = "email"
    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val CLIENT_ID = "clientId"
    const val AT_EXPIRES = "accessTokenExpires"
    const val SCOPE_ID = "scopeId"
    const val NAME = "name"
    const val OFFSET = "offset"
    const val LIMIT = "limit"

    const val GET_USERS = """
        select * from dbo.UserAccount
        offset :$OFFSET
        limit :$LIMIT
    """

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

    const val EDIT_USER = """
        update dbo.UserAccount
        set name = :$NAME
        where user_id = :$USER_ID
    """

    const val DELETE_USER = """
        delete from dbo.UserAccount
        where user_id = :$USER_ID
    """

    const val INSERT_USER_TOKEN = """
        insert into dbo.UserAccountToken 
        (access_token, refresh_token, user_id, client_id, at_expires)
        values
        (:$ACCESS_TOKEN, :$REFRESH_TOKEN, :$USER_ID, :$CLIENT_ID, :$AT_EXPIRES)
    """

    const val INSERT_USER_TOKEN_SCOPE = """
        insert into dbo.UserAccountTokenScope (token_id, scope_id)
        values
        (:$TOKEN_ID, :$SCOPE_ID)
    """

    const val GET_USER_TOKEN_SCOPES = """
        select * from dbo.UserAccountTokenScope where token_id = :$TOKEN_ID
    """

    const val GET_USER_TOKEN_BY_CLIENT = """
        select * from dbo.UserAccountToken
        where client_id = :$CLIENT_ID and user_id = :$USER_ID
    """

    const val GET_USER_BY_TOKEN = """
        select u.* 
        from (dbo.UserAccount u join dbo.UserAccountToken ut on u.user_id = ut.user_id)
        where access_token = :$ACCESS_TOKEN
    """

    const val GET_USER_TOKEN = """
        select * from dbo.UserAccountToken
        where access_token = :$ACCESS_TOKEN
    """

    const val GET_USER_TOKEN_BY_REFRESH = """
        select * from dbo.UserAccountToken
        where refresh_token = :$REFRESH_TOKEN
    """

    const val REVOKE_USER_TOKEN = """
        delete from dbo.UserAccountToken
        where access_token = :$ACCESS_TOKEN
    """

    const val REFRESH_USER_TOKEN = """
        update dbo.UserAccountToken set 
        access_token = :$ACCESS_TOKEN,
        refresh_token = :$REFRESH_TOKEN,
        at_expires = :$AT_EXPIRES,
        updated_at = now(),
        used_at = now()
        where token_id = :$TOKEN_ID
    """

    const val UPDATE_TOKEN_USED_AT = """
        update dbo.UserAccountToken set
        used_at = now()
        where token_id = :$TOKEN_ID
    """

    const val REVOKE_OLDER_TOKENS = """
        delete from dbo.UserAccountToken 
        where trunc(date_part('day', now() - used_at) / 7) >= 1;
    """
}
