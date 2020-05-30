# Intro

This document introduces the data structures used to store the issued tokens and the information associated with them.

The type of tokens used are "Reference Tokens", it means they don't include any claims or data structure in themselves.
The client sends the reference when authenticating a request, the recipient of the reference (API) must open a back-channel with the store and check the validity and claims of the token associated.


# Token Table

For the reasons mentioned before, the tokens must be stored on a table.

The tokens in the table are indexed by the SHA-256 of the token reference.

E.g. token reference
```
jXn2r5u8x/A%D*G-KaPdSgVkYp3s6v9y 
```

E.g. SHA-256 of token reference
```
06baeb46d42da63a97fd8358d5831714e95c826efe6f8d51e660457b3cb88185
```

(Hexadecimal representation, therefore, double the size of the token reference)

Using SHA-256 to index the token reference instead of plaintext has the advantage that in case of a database leak the Attacker has no knowledge of the token reference (irreversible property) and can't make changes to the database.

E.g. structure of token table:
```
CREATE TABLE dbo.Token(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hash CHAR(64) UNIQUE, -- 64 hexa chars * 4 bits per char = 256 bits hash
    isValid BOOLEAN,
    scope_id INT REFERENCES dbo.scopes(id)
)
```

# Claims Structures

Associated to the token there can be a set of claims that provide additional information.
e.g. issued at, expire time, subject...

The token claims structures:
```
CREATE TABLE dbo.iat (
    token_id INT PRIMARY KEY REFERENCES dbo.Token(id),
    issuedAt BIGINT -- miliseconds from creation since Unix epoch January 1 1970
)
```

```
CREATE TABLE dbo.exp (
    token_id INT REFERENCES dbo.Token(id),
    expireTime BIGINT 
)
```


# Policies structure
The following structure represents the allowed actions on a resource based on the HTTP method and location of the resource.
E.g. allowing GET methods for the resource /v0/courses

```
CREATE TABLE dbo.policies (
    scope_id INT PRIMARY KEY REFERENCES dbo.scopes(id), 
    method VARCHAR(10),             -- GET, POST...
    path VARCHAR(100)               -- /v0/courses?...
)
```

The allowed scopes on the application:
```
CREATE TABLE dbo.scopes (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    scope VARCHAR(100) UNIQUE -- urn:org:ionproject:scopes:api:read
)
```