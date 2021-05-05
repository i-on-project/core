package org.ionproject.core.user.auth.sql

object AuthData {

    const val AUTH_REQUEST_ID = "authRequestId"
    const val LOGIN_HINT = "loginHint"
    const val CLIENT_ID = "clientId"
    const val USER_AGENT = "userAgent"
    const val NOTIFICATION_METHOD = "notificationMethod"
    const val EXPIRES_ON = "expiration"
    const val SECRET_ID = "secretId"
    const val SCOPE = "scope"

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

    const val GET_AVAILABLE_SCOPES = """
        select * from dbo.AuthUserScope
    """

    const val INSERT_REQUEST_SCOPES = """
        insert into dbo.AuthRequestScope (auth_req_id, scope_id)
        values (:$AUTH_REQUEST_ID, :$SCOPE)
    """

    const val GET_REQUEST_SCOPES = """
        select s.scope_id, s.scope_name, s.scope_description
        from (dbo.AuthRequestScope ars join dbo.AuthUserScope s on ars.scope_id = s.scope_id)
        where ars.auth_req_id = :$AUTH_REQUEST_ID
    """
}
