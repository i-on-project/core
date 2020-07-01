
# How to obtain an import URL for a calendar?

The action of obtaining an import URL is discovered through the siren actions of the calendar resource.
To be able to obtain an import URL you also need to present, like always, a valid token with the correct scope, read in this case.

E.g. actions of the calendar associated with the course 5 of the class 1920v and class Section LI61D

```
"actions": [
    ...
    {
      "name": "import",
      "href": "/v0/import/5/1920v/LI61D/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}",
      "title": "Import calendar",
      "method": "GET",
      "isTemplated": true,
      "fields": [
        {
          "name": "type",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        },
        {
          "name": "startBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "startAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "summary",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        }
      ]
    },
    ...
  ]
```

or the following way for the calendar associated with the course of the class 1920v:

```
"actions": [
    ...
    {
      "name": "import",
      "href": "/v0/import/5/1920v/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}",
      "title": "Import calendar",
      "method": "GET",
      "isTemplated": true
    },
    "fields": [
        {
          "name": "type",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        },
        {
          "name": "startBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "startAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endBefore",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "endAfter",
          "type": "date",
          "class": "https://example.org/param/date-query"
        },
        {
          "name": "summary",
          "type": "text",
          "class": "https://example.org/param/free-text-query"
        }
      ]
    ...
  ]
```

PS: The import action also supports the search action with templating, thats why the fields are repeated.


In case the user is trying to obtain an invalid calendar (Bad Request or Not Found) he will be informed of it, instead of producing an import link that points to an error. 

When an URL import link is requested, if everything is valid the server will answer with something like:

{
  "link": "https://host1.dev.ionproject.org/v0/courses/5/classes/1920v/calendar?type=todo&access_token=mF6_7Ab..."
}


# Request and Answer using the Import Link

If the import link is valid, then the API will answer with the expected value, the calendar resource that was initially requested.
Additionaly as specified on the [RFC 6750] section 2.3. the API should also include in the answer an additional header, Cache-control with the value private.

```
Cache-Control: private
```

# Access Token Strategy

For the request to be able to be validated, it should contain the query parameter ```access_token``` that was generated and included in the URL.

The access tokens to be the most inclusive possible, and it to work with as many calendarization applications (that consume ical) as possible, was decided that its duration should be of a long period (1 year).
This is necessary because applications like outlook and google calendars synchronize their calendars every couple hours.

Another important aspect is the possibility of denial of service. The read token is public, this means that any person that has access to the android application has access to it. If the token is used wrongfully by an attacker, he could emit as many import links as he wants filling the database with trash.
This means that the url import link issuing action, can not write any information to the database, therefore the chosen way to issue import links was by signing and having the client store any necessary information.


The main problems with signed tokens is the url size and revokation, the latter is not possible in our case. The only way to kind of revoke, without storing state on the backend is by signing tokens with a low life time and having the client refresh them (which is also not possible), but as mentioned before in our case, the tokens have a long life time (1 year).
The URL size problem is negligible, such URL's are not for human consumption and are not even close to pass the limits imposed by popular browsers.


An alternative to signed tokens could be shared import links, it would make it possible to easily revoke tokens, but not without down sides. The main disvantages of this alternative is that the token to be shared, would need to be stored un-hashed on the database. The second big problem is that if even revokation is possible an attacker could take control of the shared links and constantly revoke them, denying service to legitimate clients.

Given all that, it was chosen to use signed tokens instead of strategies like shared links, but without supporting revokation. (In this case the only way of actually revoking all tokens at the same time would be by changing the secret used to sign the previous tokens or rejecting any token issued before a given time).



# Validation and JWT

The validation path of an signed token is an alternative one from the normal access manager path, its taken when the query parameter "access_token=..." is detected.

An example access token that can be found in the URI would be something like:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiIxIiwidXJpIjoiL3YwL2NvdXJzZXMvNS9jbGFzcy8xOTIwdi9jYWxlbmRhciIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjM5MDIzfQ.cqOAsYZa0YI2na2tXUwrTjoxv-8VCtklobrpZeezTL8
```

which decodes to:

```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "client_id": "1",
  "uri": "/v0/courses/5/class/1920v/calendar",
  "iat": 1516239022,
  "exp": 1516239023
}

```
(The web application found at jwt.io can be used to test this structure)

It can be seen that the access token (which in fact is a JWT) has 2 dots, each dot separes a component from each other:
Header.Payload.Signature

The header describes the structure of the token, "typ" identifies the type of token and "alg" the signing algorithm (in this case Hmac SHA256).
Those fields are only informative as the API will always enforce a signing algorithm of HMAC SHA256 ( for now at least ).

The payload contains the claims of the token, they are used to validate the request, for example the URI claim identifies to what resource the token has access.
Those fields can't be manipulated as they are protected by a signature, and the verification will fail if the user tries to manipulate the data.

The signature is generated in a way to protect the header and payload from manipulation, its operation is the following:

HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  256-bit-secret
)

The secret is stored as an environment variable of the system.

The API when receiving a request of this type will do the operation mentioned above on the token and compare it with the signature of the received token, if the comparison fails the access manager will reject the request.

If the comparison succeds the request is allowed to continue.

If for example the attacker tries to change the URI and present the token at some other endpoint besides the allowed ones (calendar), the signatures won't match and an attack attempt will be detected.
Also this kind of access is only allowed for safe operations like 'GET' or 'HEAD', if the signature is valid but the method is 'POST' the request will be rejected.

This implementation follows the RFC 7519.