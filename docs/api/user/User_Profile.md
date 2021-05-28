# User Profile Endpoints

- [Get User](#get-user)
- [Edit User](#edit-user)
- [Delete User](#delete-user)

## Get User

- Method: GET
- Endpoint: /api/users
- Required Scopes: profile
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX

### Example Request

```http
GET /api/users
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
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

- Method: PUT
- Endpoint: /api/users
- Required Scopes: profile
- Requires Authorization header with type Bearer
- Possible Responses: 204, 400, 401, 403, 5XX

### Example Request

```http
PUT /api/users
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

```json
{
    "name": "string"
}
```

### Example Response

(No Content)

## Delete User

- Method: DELETE
- Endpoint: /api/users
- Required Scopes: profile
- Requires Authorization header with type Bearer
- Possible Responses: 204, 400, 401, 403, 5XX

### Example Request

```http
DELETE /api/users
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

(No Content)