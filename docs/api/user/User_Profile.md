# User Profile Endpoints

- [Get User](#get-user)
- [Edit User](#edit-user)
- [Delete User](#delete-user)

## Get User


```http
GET /api/users
Authorization: Bearer {accessToken}
```

### Example Response

```json
{
    "class": ["user"],
    "properties": {
        "userId": "4d82659d-84e7-4061-98e1-17419c032518",
        "email": "XXXXX@alunos.isel.pt",
        "createdAt": "2021-05-28T13:31:02.033688Z"
    },
    "actions": [
        {
            "name": "edit-user",
            "href": "http://localhost:8080/api/users",
            "title": "Edit User",
            "method": "PUT",
            "type": "application/json",
            "isTemplated": true,
            "fields": [
                { "name": "name", "type": "text" }
            ]
        },
        {
            "name": "delete-user",
            "href": "http://localhost:8080/api/users",
            "title": "Delete User",
            "method": "DELETE",
            "type": "application/json",
            "isTemplated": true
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users"
        },
        {
            "rel": ["user", "class"],
            "href": "http://localhost:8080/api/users/classes"
        }
    ]
}
```

## Edit User

```http
PUT /api/users
Authorization: Bearer {accessToken}
```

```json
{
    "name": "string"
}
```

### Example Response

(No Content)

## Delete User

```http
DELETE /api/users
Authorization: Bearer {accessToken}
```

### Example Response

(No Content)