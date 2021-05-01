package org.ionproject.core.user.auth.sql

object AuthData {

    const val AUTH_REQUEST_ID = "authReqId"
    const val LOGIN_HINT = "loginHint"
    const val CLIENT_ID = "clientId"
    const val USER_AGENT = "userAgent"
    const val NOTIFICATION_METHOD = "ntfMethod"
    const val EXPIRES_ON = "expiresOn"

    const val GET_CLIENT_BY_ID = """
        select * from dbo.AuthClient
        where client_id = :$CLIENT_ID
    """

    const val GET_NOTIFICATION_METHOD = """
        select * from dbo.AuthNotificationMethod
        where method = :$NOTIFICATION_METHOD
    """

    const val INSERT_AUTH_REQUEST = """
        insert into dbo.AuthRequest 
        (auth_req_id, login_hint, user_agent, client_id, ntf_method, expires_on)
        values
        (:$AUTH_REQUEST_ID, :$LOGIN_HINT, :$USER_AGENT, :$CLIENT_ID, :$NOTIFICATION_METHOD, :$EXPIRES_ON)
    """

}