# Authentication

The user authentication flow is based on authentication methods that upon completion lead to the obtention of the `code` which implements the [`OAuth 2.0 Authorization Code Grant`](https://auth0.com/docs/flows/authorization-code-flow).

## Authentication Flow

The following diagram represents an example of the authentication procedure where `C` is the client application (for example, `i-on Android`), `S` is the i-on Core Authentication Server and `U` is the user.

This diagram presents a similar behaviour to the [OpenID Connect Client Initiated Backchannel Authentication Flow](https://openid.net/specs/openid-client-initiated-backchannel-authentication-core-1_0.html) where the client initiates the authentication flow on behalf of the user and the following user authentication interactions are done between the server and another third-party (IdP, Email Client, ...).

\#|   Flow   | HTTP Method | Description
--|:--------:|:-----------:|--------------
1 |  U -> C  |     GET     | Login Request
2 |  C -> S  |     GET     | Get Available Methods
3 |  C <- S  | ----------- | Returns the Available Methods<br>(for example `email` with the domain `alunos.isel.pt`)
4 |  U <- C  | ----------- | Presents the Methods
5 |  U -> C  | ----------- | Responds to the Authentication Method
6 |  C -> S  |     POST    | Sends Method Response with `client_id` and `state`<br>(in the context of the example it would be a student email: `AXXXXX@alunos.isel.pt`)
7 |  U -> S  |     POST    | Polling for the authentication to be completed
8 |  U <- S  | ----------- | Polling Response
..|   ...    |     ...     | ...
9 |  U -> S  |     POST    | Polling for the authentication to be completed
10|  U <- S  | ----------- | Authentication Completed or Denied

After the POST request to the server, the user will serve as an actor between the third-party and the server in order to provide proof of possession (proof that the user possesses a specified email address, for instance).

While the server is waiting for proof of possession, the client is polling the server to determine if the user has achieved authentication. This behaviour is **inspired** by the [OpenID Connect CIBA Poll Mode](https://openid.net/specs/openid-client-initiated-backchannel-authentication-core-1_0.html#rfc.section.5). In the future the Authentication Flow may support notifications through WebSockets to avoid polling the server.

### Authentication Methods

For the email authentication method the flow #2 should return:

```json
[
    {
        "type": "email",
        "allowed_domains": ["*.isel.pt", "*.isel.ipl.pt"]
    },
]
```

Where, in this case, the `allowed_domains` allows emails from the specified domains:

- Allows `isel.pt` and `isel.ipl.pt`
- Allows subdomains of `isel.pt` and `isel.ipl.pt` such as `alunos.isel.pt`

In the flow #6 the client must send the authentication request to the server. Below an example of this request is presented for the email authentication method.

```json
{
    "scope": "profile",
    "type": "email",
    "client_id": "The Client ID",
    "notification_method": "The method used to notify the client about the authentication response, for now only 'POLL' is supported",
    "login_hint": "AXXXXX@alunos.isel.pt"
}
```

For the `POLL` notification method the response is for example:

```json
{
    "auth_req_id": "Unique ID of the Authentication Request",
    "expires_in": 120,
    "interval": 2
}
```

This response follows the [Successful Authentication Request Acknowledgement](https://openid.net/specs/openid-client-initiated-backchannel-authentication-core-1_0.html#successful_authentication_request_acknowdlegment) of the OpenID Client Initiated Backchannel Authentication.

If an error has occurred the [Authentication Error Response](https://openid.net/specs/openid-client-initiated-backchannel-authentication-core-1_0.html#auth_error_response) applies.

After the flow #6 the server will send an email challenge to the user email (for example, `AXXXXX@alunos.isel.pt`), whilst the user needs to acknowledge this challenge by logging in to the `alunos.isel.pt` email server and accepting the challenge (a URL that redirects back to the server).

If the `POLL` method was chosen the user should poll the server, with the specified interval between poll requests, and the server will respond if the authentication request has been fulfilled by the user.

The responses sent by the server to the client if an error has been found, or if the user hasn't fulfilled the request follow the [OpenID Token Error Response](https://openid.net/specs/openid-connect-core-1_0.html#TokenErrorResponse).

If the user has fulfilled the authorization request the response is presented as follows:

```json
{
    "access_token": "...",
    "token_type": "Bearer",
    "refresh_token": "...",
    "expires_in": 3600,
    "id_token": "..."
}
```

Where this response follows the OpenID standard [Successful Token Response](https://openid.net/specs/openid-connect-core-1_0.html#TokenResponse).

The available error responses to the client polling event are defined in the [OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749#section-5.2), however the error codes presented in the [OAuth 2.0 Device Authorization Grant](https://tools.ietf.org/html/rfc8628#section-3.5) are also applicable as per the [OpenID CIBA Documentation](https://openid.net/specs/openid-client-initiated-backchannel-authentication-core-1_0.html#token_error_response).

## Security Considerations

The `scope` parameter must be validated for each request depending on the presented `client_id`.

A malicious user may initiate an authentication request and therefore trick a user into accepting the request. For instance, if the request is using the email method, the user must be aware of the client that initiated the request and time, so he/she can make an informed decision about allowing or denying the request, and this requires that the email is sent with enough information so the user can perform the decision.