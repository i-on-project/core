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

This "issue" token is created by the developers with gradle and a proxy tool, and only accessible to who has access to the server. It is used to issue other tokens (e.g. read and write tokens).
(Side note, if the application is running in local environment it's easier to run the task "pgInsertReadToken")

## Issue Endpoint Request

POST /issueToken
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

# Create Issue Token on GCP environment

To create an issue token and be able to issue the client tokens on the deployed environment, you need to execute the following steps:
1º Setup the GCP database proxy
./cloud_sql_proxy -instances=<ProjectName>:<Zone>:postgres-1=tcp:<port>

2º Replace the JDBC Database URL by the deployed Database configurations: (the next command only was tested on linux distributions)
export JDBC_DATABASE_URL='jdbc:postgresql://localhost:<port>/<db>?user=<user>&password=<password>'

3º Run the desired gradle task
./gradlew pgInsertIssueToken


# Token Revoke Endpoint

In case the token is leaked (or other reason), it's a good ideia to revoke it and make it unusable.
For that goal the token revoke endpoint is used.

All there is to do is make a simple PUT request to the endpoint with the token.

It's also possible to specify a special revokation operation if you are presenting a special "revoke token".
That operation is specified by adding the field "operation" to the body and as a value specify 1,2 or 3.

1: Revoke child (child here being the derived token of the read access token presented in the body)
2: Revoke presented
3: Revoke both child & presented

## Revoke Endpoint Request
POST /revokeToken
Authorization: Bearer xcL...
Content-Type: application/x-www-form-urlencoded

Body:
token=xcL...

or

Body:
token=xcL...&operation=3

The body of the request repeats the information in this phase as preparation for the next stage of the access manager where there is client authentication.

## Revoke Endpoint Response

200 Ok
Content-Type: application/json

In case an invalid token to revoke is presented, the response message will still be 200 OK. [RFC 7009]

Incase the body doesn't contain the key "token", the response is 400 Bad Request.
