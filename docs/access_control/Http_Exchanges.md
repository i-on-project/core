# Intro

The goal of this document is to specify and represent the HTTP requests in the context of the access control.

In this initial phase the token issuing endpoint is only available to the party that possesses the issue token, and the read and write tokens must be issued and distributed manually by out-of-band methods.

A final point, the write Access Token used by the Integration should not be present in the code that is uploaded to the public repository.


# Access Token

An access token, as viewed by the clients, is the base64url encode of a 256 bit (?) random string.

E.g. random 256 bit string (UTF-8 encoding):
```
jXn2r5u8x/A%D*G-KaPdSgVkYp3s6v9y 
```

Base64url encode of the previous access token:
```
alhuMnI1dTh4L0ElRCpHLUthUGRTZ1ZrWXAzczZ2OXkg
```

To this token there are associated a set of claims further explained in the SQL Access Control document, one of the claims is the scope authorized to the client.

Using 256 bits means that there are 1.1579209x10^77 possible token combinations, making it infeasible to brute-force.

# Requests
All requests should be subject to access control, including read requests.

The token is sent to the API throught the Authorization request header using the "Bearer" authentication scheme. [RFC 6750]

E.g. request to obtain the courses:

```
GET /courses HTTP/1.1
Host: example.com
Authorization: Bearer alhuMnI1dTh4L0ElRCpHLUthUGRTZ1ZrWXAzczZ2OXkg
```

# Responses
If the token is valid (not revoked, not expired, sufficient privileges...) the policy enforcement system will act accordingly and return the expected course siren representation results.

In case the token is invalid (the authorization header is missing, ...) an error response message will be returned.


## Invalid Request:
Occurs when the requests uses a token include type that is not supported (something else besides "Bearer" type).

```
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json
{
    title: "Invalid Request",
    status: 400,
    detail: "Unsupported include token type.",
    instance: "/example"
}
```

## Invalid Token:
Occurs when the token is invalid (revoked, expired...).

```
HTTP/1.1 401 Unauthorized
Content-Type: application/problem+json
{
    title: "Invalid Token",
    status: 401,
    detail: "Token is not valid, it may have expired... try requesting a new one.",
    instance: "/example"
}
```

## Insufficient Scope:
Occurs when the request requires higher privileges than provided by the access token.

```
HTTP/1.1 403 Forbidden
Content-Type: application/problem+json
{
    title: "Invalid Scope",
    status: 403,
    detail: "You Require higher privileges to do that.",
    instance: "/example"
}
```

## Lacks authentication:
Occurs if the client doesn't include the Authorization header with the token, it must return the response header WWW-Authenticate.

```
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="example"
Content-Type: application/problem+json
{
    title: "Authentication Required",
    status: 401,
    detail: "Your request lacks authentication.",
    instance: "/example"
}
```

# Token Issue Endpoint

The token issue endpoint creates a token with the scope that it was requested in the body.

In the beta version this endpoint is only available to whom possesses the correct token with the scope "issue".

This "issue" token is created during the boot process of the web application, and only accessible to who has access to the server. It is used to issue other tokens (e.g. read and write tokens).
(Side note, if the application is running in local environment it's easier to run the task "pgInsertReadToken")

## Issue Endpoint Request

PUT /issueToken
Host: example.com
Authorization: Bearer (issue token)
Content-Type: application/json

Body:
{
    scope: "issue"
}

## Issue Endpoint Response

Example of response to a successful request:

200 OK
Content-Type: application/json

Body:
{
    token: "xCl...",
    issuedAt: 1591544539044
}

The error messages are the same messages already defined in the general requests section.
Example, trying to access this endpoint with a token that has an invalid scope will result in an error 403 FORBIDDEN.


# Token Revoke Endpoint

In case the token is leaked (or other reason), it's a good ideia to revoke it and make it unusable.
For that goal the token revoke endpoint is used.

All there is to do is make a simple PUT request to the endpoint with the token.

## Revoke Endpoint Request
PUT /revokeToken
Authorization: Bearer (token)


## Revoke Endpoint Response

200 Ok
Content-Type: application/json

Body:
{
    result: "Token revoked"
}

The error messages are the same messages already defined in the general requests section.
Example, trying to revoke an unexistent token would result in a 401 Unauthorized.
