# User Classes Endpoints

- Classes
    - [Get Subscribed Classes](#get-subscribed-classes)
    - [Get Subscribed Class](#get-subscribed-class)
    - [Subscribe to Class](#subscribe-to-class)
    - [Unsubscribe from Class](#unsubscribe-from-class)
- Class Sections
    - [Get Subscribed Class Section](#get-subscribed-class-section)
    - [Subscribe to Class Section](#subscribe-to-class-section)
    - [Unsubscribe from Class Section](#unsubscribe-from-class-section)

## Get Subscribed Classes

- Method: GET
- Endpoint: /api/users/classes
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX
- Includes pagination

### Example Request

```http
GET /api/users/classes
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

```json
{
    "class": ["user", "class", "collection"],
    "properties": {
        "page": 0,
        "limit": 10
    },
    "entities": [
        {
            "rel": ["item"],
            "class": ["user", "class"],
            "properties": {
                "id": 16,
                "courseId": 1,
                "courseAcr": "SL",
                "calendarTerm": "1920i"
            },
            "links": [
                {
                    "rel": ["self"],
                    "href": "http://localhost:8080/api/users/classes/16"
                },
                {
                    "rel": ["/rel/class"],
                    "href": "http://localhost:8080/api/courses/1/classes/1920i"
                }
            ]
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/classes?page=0&limit=10"
        },
        {
            "rel": ["next"],
            "href": "http://localhost:8080/api/users/classes?page=1&limit=10"
        }
    ]
}
```

## Get Subscribed Class

- Method: GET
- Endpoint: /api/users/classes/{classId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX

### Example Request

```http
GET /api/users/classes/16
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

```json
{
    "class": ["user", "class"],
    "properties": {
        "id": 16,
        "courseId": 1,
        "courseAcr": "SL",
        "calendarTerm": "1920i"
    },
    "entities": [
        {
            "rel": ["item"],
            "class": ["user", "class", "section"],
            "properties": {
                "sectionId": "1D"
            },
            "links": [
                {
                    "rel": ["self"],
                    "href": "http://localhost:8080/api/users/classes/16/1D"
                },
                {
                    "rel": ["/rel/classSection"],
                    "href": "http://localhost:8080/api/courses/1/classes/1920i/1D"
                }
            ]
        }
    ],
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/classes/16"
        },
        {
            "rel": ["/rel/class"],
            "href": "http://localhost:8080/api/courses/1/classes/1920i"
        },
        {
            "rel": ["/rel/userClassActions"],
            "href": "http://localhost:8080/api/users/actions/classes/16"
        }
    ]
}
```

## Subscribe to Class

- Method: PUT
- Endpoint: /api/users/classes/{classId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 201, 204, 400, 401, 403, 5XX

### Example Request

```http
PUT /api/users/classes/16
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

(Created or No Content)

## Unsubscribe from Class

- Method: DELETE
- Endpoint: /api/users/{classId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX

### Example Request

```http
DELETE /api/users/classes/16
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

(No Content)

## Get Subscribed Class Section

- Method: GET
- Endpoint: /api/users/classes/{classId}/{sectionId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX

### Example Request

```http
GET /api/users/classes/16/1D
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

```json
{
    "properties": {
        "id": "1D",
        "courseId": 1,
        "courseAcr": "SL",
        "calendarTerm": "1920i"
    },
    "links": [
        {
            "rel": ["self"],
            "href": "http://localhost:8080/api/users/classes/16/1D"
        },
        {
            "rel": ["/rel/classSection"],
            "href": "http://localhost:8080/api/courses/1/classes/1920i/1D"
        },
        {
            "rel": ["/rel/userClassSectionActions"],
            "href": "http://localhost:8080/api/users/actions/classes/16/1D"
        }
    ]
}
```

## Subscribe to Class Section

- Method: PUT
- Endpoint: /api/users/classes/{classId}/{sectionId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 201, 204, 400, 401, 403, 5XX

### Example Request

```http
PUT /api/users/classes/16/1D
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

(Created or No Content)

## Unsubscribe from Class Section

- Method: DELETE
- Endpoint: /api/users/classes/{classId}/{sectionId}
- Required Scopes: classes
- Requires Authorization header with type Bearer
- Possible Responses: 200, 400, 401, 403, 5XX

### Example Request

```http
DELETE /api/users/classes/16/1D
Authorization: Bearer q-My3nteC0GOC1N90OeMHKNzrwO5ZdiuAGb8K9x7fG5hezHkDSBRzilna3IgaTXzz7fqTRsdUkKl9ith3TsVEQ
```

### Example Response

(No Content)