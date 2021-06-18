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

```http
GET /api/users/classes
Authorization: Bearer {accessToken}
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

```http
GET /api/users/classes/{classId}
Authorization: Bearer {accessToken}
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


```http
PUT /api/users/classes/{classId}
Authorization: Bearer {accessToken}
```

### Example Response

(Created or No Content)

## Unsubscribe from Class

```http
DELETE /api/users/classes/{classId}
Authorization: Bearer {accessToken}
```

### Example Response

(No Content)

## Get Subscribed Class Section

```http
GET /api/users/classes/{classId}/{classSectionId}
Authorization: Bearer {accessToken}
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

```http
PUT /api/users/classes/{classID}/{classSectionId}
Authorization: Bearer {accessToken}
```

### Example Response

(Created or No Content)

## Unsubscribe from Class Section

```http
DELETE /api/users/classes/{classId}/{classSectionId}
Authorization: Bearer {accessToken}
```

### Example Response

(No Content)