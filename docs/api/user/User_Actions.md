# User Actions Endpoint

## Classes and Class Sections

### Class Actions

```http
GET /api/users/actions/classes/{classId}
Authorization: Bearer {accessToken}
```

#### Example Response

```json
{
    "class": ["user", "class", "action"],
    "actions": [
        {
            "name": "subscribe",
            "href": "http://localhost:8080/api/users/classes/16",
            "title": "Subscribe to Class",
            "method": "PUT"
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/actions/classes/16"
        }
    ]
}
```

Or if already subscribed:

```json
{
    "class": ["user", "class", "action"],
    "actions": [
        {
            "name": "unsubscribe",
            "href": "http://localhost:8080/api/users/classes/16",
            "title": "Unsubscribe From Class",
            "method": "DELETE"
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/actions/classes/16"
        }
    ]
}
```

### Class Section Actions

```http
GET /api/users/actions/classes/{classId}/{classSectionId}
Authorization: Bearer {accessToken}
```

#### Example Response

```json
{
    "class": ["user", "class", "section", "action"],
    "actions": [
        {
            "name": "subscribe",
            "href": "http://localhost:8080/api/users/classes/16/1D",
            "title": "Subscribe to Class Section",
            "method": "PUT"
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/actions/classes/16/1D"
        }
    ]
}
```

Or if already subscribed:

```json
{
    "class": ["user", "class", "section", "action"],
    "actions": [
        {
            "name": "unsubscribe",
            "href": "http://localhost:8080/api/users/classes/16/1D",
            "title": "Unsubscribe From Class Section",
            "method": "DELETE"
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/actions/classes/16/1D"
        }
    ]
}
```