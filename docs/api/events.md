# __ALL URIS ARE SUBJECT TO CHANGE AND NOT FINAL IN ANY WAY__

## GET /v0/courses/{course_id}/classes/{class_id}/events

Retrieves the collection of `Event`s associated with the given `Class`

Method: GET

Path: /v0/courses/{course_id}/classes/{term_id}/events

Path Arguments:
- course_id
    - Id of the course
- term_id
    - Id of the term

Body: Could have queries if the user does not want to or cannot send them via the query string.
- Type: __application/x-www-form-urlencoded__

Queries:
- startBefore: Filter by events that have an earlier `start_date`
    - Type: date
    - Help: https://example.org/param/date-query
- startAfter: Filter by events that have a later `start_date`
    - Type: date
    - Help: https://example.org/param/date-query
- endBefore: Filter by events that have a earlier `end_date`
    - Type: date
    - Help: https://example.org/param/date-query
- endBefore: Filter by events that have a later `end_date`
    - Type: date
    - Help: https://example.org/param/date-query

Success
- Status Code: __200__

Error
- Not Found: If either the `Course` or the `Class` doesn't exist.
    - Status Code: __404__

Example
- Request
```
GET /v0/courses/daw/classes/s1920v/events HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "collection", "event" ],
    "properties": { 
        "size": 2
    },
    "entities": [
        {
            "class": [ "event" ],
            "rel": [ "item" ],
            "properties": {
                "event_id": 1234,
                "title": "Exame DAW 1",
                "description": "Exame de época normal do semestre 1920v",
                "start_date": "19-06-2020 14:00",
                "end_date": "19-06-2020 16:30"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events/1234"},
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v"}
            ]
        },
        {
            "class": [ "event" ],
            "rel": [ "item" ],
            "properties": {
                "event_id": 1235,
                "title": "Exame DAW 2",
                "description": "Exame de 2ª época do semestre 1920v",
                "start_date": "30-06-2020 14:00",
                "end_date": "30-06-2020 16:30"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events/1235"},
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v"}
            ]
        }
    ],
    "actions": [
        {
            "name": "search",
            "title": "Procurar",
            "method": "GET",
            "href": "/v0/courses/daw/classes/s1920v/events{?startBefore,startAfter,endBefore,endAfter,title}",
            "isTemplated": true,
            "type": "application/x-www-form-urlencoded",
            "fields": [
                { "name": "startBefore", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "startAfter", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "endBefore", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "endAfter", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "title", "type": "text", "class": "https://example.org/param/free-text-query"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events?page=1" },
        { "rel": [ "next" ], "href": "/v0/courses/daw/classes/s1920v/events?page=2" },
        { "rel": [ "previous" ], "href": "/v0/courses/daw/classes/s1920v/events?page=0" }
    ]
} 
```


## GET /v0/courses/{course_id}/classes/{term_id}/events/{event_id}

Retrieves the specified `Event` of the given `Class`

__Method__: GET

__Path__: /v0/courses/{course_id}/classes/{term_id}/events/{event_id}

__Path Arguments__:
- course_id
    - Id of the `Course`
- term_id
    - Id of the `Term`
- event_id
    - Id of the `Event`

__Body__: __Empty__

__Queries__: __No queries supported.__

__Success__
- Status Code: __200__

__Error__
- Not Found: If either the `Course`, `Class` or the `Event` doesn't exist.
    - Status Code: __404__

__Example__
- Request
```
GET /v0/courses/daw/classes/s1920v/events/1234 HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "event" ],
    "properties": { 
        "event_id": 1234,
        "title": "Exame DAW 1",
        "description": "Exame de época normal do semestre 1920v",
        "start_date": "19-06-2020 14:00",
        "end_date": "19-06-2020 16:30"
    },
    "entities": [
        {
            "class": [ "class" ],
            "rel": [ "https://example.com/rels/class" ],
            "properties": {
                "class_id": "daw-s1920v",
                "term_id": "s1920v"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v"},
                { "rel": [ "term" ], "href": "/v0/terms/s1920v"},
                { "rel": [ "course"], "href": "/v0/courses/daw"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/1920v/events/1234" },
    ]
} 
```

## GET /v0/courses/{course_id}/classes/{class_id}/sections/{section_id}/events

Retrieves the collection of `Event`s associated with the given `ClassSection`

__Method__: GET

__Path__: /v0/courses/{course_id}/classes/{term_id}/sections/{section_id}/events

__Path Arguments:__
- course_id
    - Id of the `Course`
- term_id
    - Id of the `Term`
- section_id
    - Id of the `ClassSection`

__Body:__ Could have queries if the user does not want to or cannot send them via the query string.
- Type: __application/x-www-form-urlencoded__

__Queries:__
- startBefore: Filter by events that have an earlier `start_date`
    - Type: date
    - Help: https://example.org/param/date-query
- startAfter: Filter by events that have a later `start_date`
    - Type: date
    - Help: https://example.org/param/date-query
- endBefore: Filter by events that have a earlier `end_date`
    - Type: date
    - Help: https://example.org/param/date-query
- endBefore: Filter by events that have a later `end_date`
    - Type: date
    - Help: https://example.org/param/date-query

__Success__
- Status Code: __200__

__Error__
- Not Found: If either the `Course`, `Class` or the `ClassSection` doesn't exist.
    - Status Code: __404__

__Example__
- Request
```
GET /v0/courses/daw/classes/s1920v/sections/61D/events HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "collection", "event" ],
    "properties": { 
        "size": 2
    },
    "entities": [
        {
            "class": [ "event" ],
            "rel": [ "item" ],
            "properties": {
                "event_id": 456,
                "title": "Série 1",
                "description": "",
                "start_date": "19-06-2020 14:00",
                "end_date": "19-06-2020 16:30"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events/1234"},
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v"}
            ]
        },
        {
            "class": [ "event" ],
            "rel": [ "item" ],
            "properties": {
                "event_id": 1235,
                "title": "Exame DAW 2",
                "description": "Exame de 2ª época do semestre 1920v",
                "start_date": "30-06-2020 14:00",
                "end_date": "30-06-2020 16:30"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events/1235"},
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v"}
            ]
        }
    ],
    "actions": [
        {
            "name": "search",
            "title": "Procurar",
            "method": "GET",
            "href": "/v0/courses/daw/classes/s1920v/events{?startBefore,startAfter,endBefore,endAfter,title}",
            "isTemplated": true,
            "type": "application/x-www-form-urlencoded",
            "fields": [
                { "name": "startBefore", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "startAfter", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "endBefore", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "endAfter", "type": "date", "class": "https://example.org/param/date-query"},
                { "name": "title", "type": "text", "class": "https://example.org/param/free-text-query"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/events?page=1" },
        { "rel": [ "next" ], "href": "/v0/courses/daw/classes/s1920v/events?page=2" },
        { "rel": [ "previous" ], "href": "/v0/courses/daw/classes/s1920v/events?page=0" }
    ]
} 
```

## GET /v0/courses/{course_id}/classes/{class_id}/sections/{section_id}/schedule

Retrieves the `Schedule` associated with the given `ClassSection`

__Method__: GET

__Path__: /v0/courses/{course_id}/classes/{term_id}/sections/{section_id}/schedule

__Path Arguments:__
- course_id
    - Id of the `Course`
- term_id
    - Id of the `Term`
- section_id
    - Id of the `ClassSection`

__Body: Empty__

__Queries: No queries supported.__

__Success__
- Status Code: __200__

__Error__
- Not Found: If either the `Course`, `Class` or the `ClassSection` doesn't exist.
    - Status Code: __404__

__Example__
- Request
```
GET /v0/courses/daw/classes/s1920v/sections/61D/schedule HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "schedule" ],
    "properties": { 
        "start_date": "26/02/2020",
        "end_date": "15/06/2020",
        "lectures": [
            {
                "weekday": "Monday",
                "start_time": "11:00",
                "end_time": "12:30"
            },
            {
                "weekday": "Thursday",
                "start_time": "11:00",
                "end_time": "14:00"
            }
        ]
    },
    "entities": [
        {
            "class": [ "class-section" ],
            "rel": [ "https://example.com/rels/class-section" ],
            "properties": {
                "id": "61D",
                "lecturer": 1010
            },
            "links": [
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v" },
                { "rel": [ "related" ], "href": "/v0/lecturers/1010"}
            ]
        }
    ],
    "actions": [],
    "links": [
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/sections/61D/schedule" },
    ]
} 
```