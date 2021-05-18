# Authentication Endpoints

This document describes how the User API can be used to authenticate new or existing users. This authentication process allows to verify the user identity by using, for example, an email associated with the institution that the user is attending (a student email).

The draft of this procedure, that was the basis to the construction of this API, is explained in the [Authentication Implementation](../../implementation/user_api/Authentication.md) document.

## Retrieving Authentication Methods

To start the authentication procedure a client app needs to retrieve the available authentication methods (for instance, the email method).

```
Method: GET
Endpoint: /api/auth/methods
```

Where the response is a json array with all of the available methods at that time:

```json
[
    {
        // The allowed domains for this method
        // The wildcard means that any email ending in isel.pt or isel.ipl.pt is allowed
        "allowed_domains": [
            "*.isel.pt",
            "*.isel.ipl.pt"
        ],
        // The type of this authentication method
        "type": "email",
        // True if this method can be used to create new accounts. If false this method can only be used if an account already exists
        "create": true
    }
]
```

## Selecting the Authentication Method

After the user choses the authentication method they want to use, the client app must send the following request to continue the authentication procedure:

```
Method: POST
Endpoint: /api/auth/methods
``` 

With the following `application/json` body:

```json
{
    // The necessary scopes. For now only "profile" is available
    "scope": "profile",
    // Chosen authentication method
    "type": "email",
    // The unique id of the client provided by the core system
    "client_id": "22dd1551-db23-481b-acde-d286440388a5",
    // A valid notification method. Only "POLL" is supported
    "notification_method": "POLL",
    // The email of the new or existing user
    "email": "A46074@alunos.isel.pt"
}
```

The response for this request should be:

```json
{
    // The authentication request id that the client id should delegate to the user view, in order to make the POLL request
    "auth_req_id": "bff43137-95a6-4ef0-afbf-906652695e1b",
    // The expiration, in seconds, of this authentication request (5 minutes, in this case)
    "expires_in": 300
}
```

## Detecting Request Completion

To detect the request completion the user should use the notification methods that were provided. Since only `"POLL"` is supported at the moment the user should send multiple `GET` requests to a special URI that will inform if the authentication procedure was completed, returning the necessary tokens for access control.

### Using POLL

```
Method: GET
Endpoint: /api/auth/request/bff43137-95a6-4ef0-afbf-906652695e1b/poll
```

If the request has been completed the response will be:

```json
{
    // The access_token used for access control
    "access_token": "pQchLzPAxRH6SedOVPU-69d_vzuoGY-yjzhCRBLD2sQUS4sq9l0wvXhOoobhM9Tgepn-__YpcRhzJ7KcoENCgQ",
    // Type of the access token that was provided
    "token_type": "Bearer",
    // Refresh token used to refresh the access token when it expires
    "refresh_token": "QonN7XoEfh-1DZ-vTUBEycrDBBDQ0ppVJzNK5PHawk9WVkYkUIdCXrpVYYfGiTXJQhZVB5pEp7LlCba5vbbAEw",
    // Seconds to the access token expiration date
    "expires_in": 10799,
    // The id token, containing several claims about the authenticated user
    "id_token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA2NzI4OTQsImF1ZCI6IjIyZGQxNTUxLWRiMjMtNDgxYi1hY2RlLWQyODY0NDAzODhhNSIsImlhdCI6MTYyMDY2OTI5NCwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaSIsInN1YiI6ImE2MjhlZWM1LWI5NGUtNDc2OC1hODUwLTYzNjJmZDY4YzVkZiIsImVtYWlsIjoiQTQ2MDc0QGFsdW5vcy5pc2VsLnB0In0.8uuKhkfDN0YuOtIhSbwDkCl5g0OX-QiBp4dvCGhMY0s"
}
```

If there was an error (the request is still pending, for instance) the response will be as defined in the [Authentication Implementation](../../implementation/user_api/Authentication.md) document.

## Refreshing the Access Token

To refresh a access token a client application can use the provided endpoint:

```
Method: POST
Endpoint: /api/auth/refreshToken
```

with the following request body:

```json
{
    // The access token to refresh
    "access_token": "pQchLzPAxRH6SedOVPU-69d_vzuoGY-yjzhCRBLD2sQUS4sq9l0wvXhOoobhM9Tgepn-__YpcRhzJ7KcoENCgQ",
    // The refresh token associated with the provided access token
    "refresh_token": "QonN7XoEfh-1DZ-vTUBEycrDBBDQ0ppVJzNK5PHawk9WVkYkUIdCXrpVYYfGiTXJQhZVB5pEp7LlCba5vbbAEw"
}
```

where the response will look like:

```json
{
    "access_token": "...",
    "token_type": "Bearer",
    "refresh_token": "...",
    "expires_in": 10799,
    "id_token": "..."
}
```

Note that you can only refresh the access token once each 20 minutes. Retrieving the first instance of the access token (with `POLL` for instance) also counts to this rate limit therefore, in practice, the limit has a delay of 20 minutes with a subsequent interval of 20 minutes, starting from the date that the access token was emitted.

This rate limit is implementation specific and depends on the expiration of the access token, and when it applies the returned response will be a `429 Too Many Requests` with the following body: 

```json
{
    "error": "slow_down",
    "error_description": "You can only refresh this token once each 20 minutes"
}
```

## Revoking an Access Token

If the client wants to revoke the access token for some reason, such as a database leak, it can do so by:

```
Method: DELETE
Endpoint: /api/auth/revokeToken
```

and, if successful, the response will be `204 No Content`.

## Internal Endpoints

This section describes endpoints that should only be used internally, with no prejudice if they're used by another application.

### Verify Authentication Request

Validates the specified authentication request with the associated secret id.

```
Method: POST
Endpoint: /api/auth/verify
```

With the body being:

```json
{
    // The unique id of the authentication request to be validated
    "auth_req_id": "85b594fa-6ab4-4e7d-98e3-cfe7789b974b",
    // The secret associated with the authentication request
    "secret": "MkrVhxjAhH_ok-IKAGDbhSZ1jTAm3qSnHOqr56EtDDycrqB8GtIdiqq1j9o6P2FIh3sZV-X-69uRshKHOWWoKw"
}
```

If successful a `204 No Content` status code is returned.

### Get Authentication Request

Provides in depth information about the authentication request with the specified id.

```
Method: GET
Endpoint: /api/auth/request/85b594fa-6ab4-4e7d-98e3-cfe7789b974b
```

With the following response:

```json
{
    // Unique Id of the authentication request
    "auth_req_id": "85b594fa-6ab4-4e7d-98e3-cfe7789b974b",
    // The Zulu (Z, UTC) expiration of this authentication request
    "expires_on": "2021-05-10T18:20:49.317540Z",
    // Details about the client that started the procedure
    "client": {
        "client_id": "22dd1551-db23-481b-acde-d286440388a5",
        "client_name": "i-on Web",
        "client_url": "https://i-on-web.herokuapp.com/"
    },
    // Details about the requested scopes
    "scopes": [
        {
            "scope_id": "profile",
            "scope_name": "Access to profile",
            "scope_description": "Grants the client app access to basic profile information such as name and email."
        }
    ],
    // The post action URI used to send the verification request
    "verify_action": "http://localhost:8080/api/auth/verify"
}
```
