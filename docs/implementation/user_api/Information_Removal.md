# Information Removal

This document describes in a first part how the user should be able to request its information removal, and in the second part it's analyzed how should the system remove inactive users.

## User-Requested Removal

The user should have at its disposal a way to completely remove its information from the i-on Core system. This is required under the European Union GDPR, for example.

To that end it was created the following information removal flow, that represents the necessary steps to remove user information at a user's request, where `U` is the user, `C` is the client and `S` is the i-on Core:

#|  Flow  | Description
-|:------:|-------------
1| U -> C | Requests information removal
2| C -> S | Requests information removal challenge
3| S -> C | Replies with the necessary challenge
4| C -> U | Presents the challenge to the user
5| U -> C | Acknowledges or cancels the challenge
6| C -> S | Sends the challenge response

The challenge sent by the server should be in the following format:

```json
{
    "id": "Unique ID for the challenge",
    "challenge": "octopus"
}
```

Where `id` is the server-side identificator for this challenge (associated with the user account) and `challenge` is the word challenge the user must complete in order to remove its information.

The client will present the challenge to the user, whilst the user needs to write the challenge word into a text box. This process is inspired by GitHub's own challenge for dangerous actions, such as, repository deletion.

Upon completion of the challenge a request is sent to the server with the `id` of the challenge and the **`word` that the user has typed in**.

```json
{
    "id": "Unique ID for the challenge",
    "typed_word": "not_octopus"
}
```

In this example, since both words don't match the server will deny the account removal and will return a *403 Forbidden*. Otherwise the server would respond with a *204 No Content* status code.

## Removal of Inactive Users

The system should be able to remove inactive users, that may not belong anymore to the academic institution, and so, those must be deleted from the system.

To this end i-on Core must have a cron task that checks periodically for inactive users (>1 year of inactivity, for instance), marks the account for deletion and attempts to contact the user through its email during a grace period of, for example, 15 days. After the grace period has passed the account and associated resources are removed from the server.

In the case that the academic institution wants to remove a user from the system due, for instance, the user doesn't attent the institution anymore and thus resources can be freed, the User API should make available an endpoint for this end.