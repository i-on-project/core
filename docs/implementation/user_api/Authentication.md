# Authentication

The user authentication flow is based on challenges that upon completion lead to the obtention of the `access_token` which implements the [`OAuth 2.0 Authorization Code Grant`](https://auth0.com/docs/flows/authorization-code-flow).

## Authentication Flow

The following diagram represents an example of the authentication procedure where `C` is the client application (for example, `i-on Android`), `S` is the i-on Core Authentication Server and `U` is the user.

#|   Flow   | Description
-|:--------:|-------------
1|  U -> C  | Login Request
2|  C -> S  | Get Available Challenges
3|  S -> C  | Returns the Available Challenges<br>(for example `email` with the domain `alunos.isel.pt`)
4|  C -> U  | Presents the Challenge
5|  U -> C  | Responds to the Challenge
6|  C -> S  | Sends Challenge Response with `client_id`, `redirect_uri` and `state`<br>(in the context of the example it would be a student email: `AXXXXX@alunos.isel.pt`)
7|  S -> C  | Redirects to verified `redirect_uri` with `code` and `state`
8|  C -> S  | Validates the `state` and Retrieves the `access_token`, `refresh_token` and `id_token` with `code`
9|  S -> C  | Responds with `access_token`, `refresh_token`, `id_token` and `expires_in`

However, this diagram can vary depending on the chosen challenge. In the next section we take into consideration available challenges and their needs.

### Challenges

For the email challenge the flow #2 should return:

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

After the flow #6 the server will send an email challenge to the user email (for example, `AXXXXX@alunos.isel.pt`), whilst the user needs to acknowledge this challenge by logging in to the `alunos.isel.pt` email server and accepting the challenge (a URL that redirects back to the server).

Following the challenge acceptance or denial (due to a timeout, for example) the user is redirected back to the client, as demonstrated in the flow #7.

The client will then proceed to verify the received state that is used against *Cross-Site Request Forgery* attacks and uses the received `code` to obtain the `access_token`, among others.

After receiving the request for the `access_token` the server validates the `code` and responds with the following information:

```json
{
    "access_token": "...",
    "id_token": "JWT that contains claims about the user",
    "refresh_token": "Token used to refresh the access_token when it expires",
    "expires_in": "Seconds remaining to the expiration of the access_token"
}
```

## Security Considerations

Unlike `OAuth 2.0 Authorization Code Grant` the `state` parameter is mandatory, since it provides security against `CSRF` attacks.

The `redirect_uri` must be associated with the `client_id` (i.e the `client_id` has a list of valid `redirect_uri` that includes the `redirect_uri` that was sent). This is done in order to prevent CSRF attacks once again, since a malicious individual could send a malicious `redirect_uri` with a trusted `client_id`, possible leading to the user logging in and, therefore, retrieving the `code` and `access_token`.