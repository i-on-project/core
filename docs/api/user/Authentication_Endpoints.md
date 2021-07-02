# Authentication Endpoints

This document describes how the User API can be used to authenticate new or existing users. This authentication process allows to verify the user identity by using, for example, an email associated with the institution that the user is attending (a student email).

The draft of this procedure, that was the basis to the construction of this API, is explained in the [Authentication Architecture](../../architecture/user_api/Authentication.md) document.

## Retrieving Authentication Methods

To start the authentication procedure a client app needs to retrieve the available authentication methods (for instance, the email method).

```http
GET /api/auth/methods
```

Where the response is a json array with all of the available methods at that time:

```json
[
    {

        "allowed_domains": [
            "*.isel.pt",
            "*.isel.ipl.pt"
        ],
        "type": "email"
    }
]
```

## Initiating Backchannel Authentication

After the user choses the authentication method they want to use, the client app must send the following request to initiate the authentication procedure:

```http
POST /api/auth/backchannel
``` 

With the following `application/json` body:

```json
{
    "scope": "The necessary scopes, separated by spaces",
    "acr_values": "The chosen authentication methods, separated by space. Only 'email' is available for now",
    "login_hint": "The user institutional email",
    "client_id": "The client id of the application",
    "client_secret": "Is optional and only used if the application is confidential"
}
```

A successful response to this request is:

```json
{
    "auth_req_id": "The authentication request id",
    "expires_in": 300
}
```

## Detecting Request Completion

To detect the request completion the user should use the notification methods that were provided. Since only `"POLL"` is supported at the moment the user should send multiple `POST` requests to a special URI that will inform if the authentication procedure was completed, returning the necessary tokens for access control.

### Using POLL

```http
POST /api/auth/token
```

With the following response body:

```json
{
    "grant_type": "urn:openid:params:grant-type:ciba",
    "auth_req_id": "The id of the authentication request",
    "client_id": "The client id of the application",
    "client_secret": "Is optional and only used if the application is confidential"
}
```

If the authentication request was completed the response will be:

```json
{
    "access_token": "The access token",
    "token_type": "Bearer",
    "refresh_token": "The refresh token, used to refresh the access token",
    "expires_in": 10799,
    "id_token": "The id token, containing a series of assertions about the user"
}
```

## Refreshing the Access Token

To refresh a access token a client application can use the provided endpoint:

```http
POST /api/auth/token
```

with the following request body:

```json
{
    "grant_type": "refresh_token",
    "refresh_token": "The refresh token that was granted to the client application",
    "client_id": "The client id of the application",
    "client_secret": "Is optional and only used if the application is confidential"
}
```

where a successful response will look like:

```json
{
    "access_token": "The new access token",
    "token_type": "Bearer",
    "refresh_token": "The new refresh token, used to refresh the new access token",
    "expires_in": 10799,
    "id_token": "The id token, containing a series of assertions about the user"
}
```

Note that refresh the access token is subject to rate limiting.

This rate limit is implementation specific and depends on the expiration of the access token, and when it applies the returned response will be a `429 Too Many Requests` with the following body: 

```json
{
    "error": "slow_down",
    "error_description": "You can only refresh this token once each X minutes"
}
```

## Revoking an Access Token

If the client wants to revoke the access token for some reason, such as a database leak, it can do so by:

```http
DELETE /api/auth/revoke
```

with the following body:

```json
{
    "token": "The access token to be revoked",
    "client_id": "The client id of the application",
    "client_secret": "Is optional and only used if the application is confidential"
}
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
    "auth_req_id": "The unique id of the authentication request to be validated",
    "secret": "The secret associated with the authentication request"
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
    "auth_req_id": "The id of the authentication request",
    "expires_on": "Expiration date of the auth request",
    "client": {
        "client_id": "The id of the client",
        "client_name": "The name of the client",
        "client_url": "The url of the client, if any",
        "confidential": true
    },
    "scopes": [
        {
            "scope_id": "An id of a requested scope",
            "scope_name": "The name of the requested scope",
            "scope_description": "Description of the requested scope"
        }
    ],
    "verify_action": "An URI to the authentication verification endpoint"
}
```
