# Intro

The goal of this document is to specify and represent the HTTP requests in the context of the access control.

In this initial phase the token issuing endpoint is not used as the client applications have the tokens statically coded.

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