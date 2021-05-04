package org.ionproject.core.user.auth.sql

object AuthData {

    const val AUTH_REQUEST_ID = "authReqId"
    const val LOGIN_HINT = "loginHint"
    const val CLIENT_ID = "clientId"
    const val USER_AGENT = "userAgent"
    const val NOTIFICATION_METHOD = "ntfMethod"
    const val EXPIRES_ON = "expiresOn"
    const val SECRET_ID = "secretId"

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
        (auth_req_id, secret_id, login_hint, user_agent, client_id, ntf_method, expires_on)
        values
        (:$AUTH_REQUEST_ID, :$SECRET_ID, :$LOGIN_HINT, :$USER_AGENT, :$CLIENT_ID, :$NOTIFICATION_METHOD, :$EXPIRES_ON)
    """

    const val GET_AUTH_REQUEST = """
        select * from dbo.AuthRequest
        where auth_req_id = :$AUTH_REQUEST_ID
    """

    const val REMOVE_AUTH_REQUEST = """
        delete from dbo.AuthRequest
        where auth_req_id = :$AUTH_REQUEST_ID
    """

    const val VERIFY_AUTH_REQUEST = """
        update dbo.AuthRequest
        set verified = true
        where auth_req_id = :$AUTH_REQUEST_ID
    """

}