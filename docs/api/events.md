# __ALL URIS ARE SUBJECT TO CHANGE AND NOT FINAL IN ANY WAY__

## Event

An `Event` describes an ocurrence within a given timeframe, such as an exam, a meeting or an appointment. 

__Properties:__
- id: the unique identifier of this `Event`
  - Type: integer
- title: title of the `Event`
  - Type: text
  - e.g. DAW 1st Exam
- description: description of the `Event`
  - Type: text
  - e.g. "First exam for the DAW-s1920v class."
- start_date: starting date of the `Event`
  - Type: date
  - e.g. 19/03/2020
- end_date: ending date of the `Event`
  - Type: date
  - e.g. 30/04/2020
  - Notes: 
    - the end_date should be after start_date

__Link Relations:__
- self
- about: the context of the schedule
  - e.g. If the `Event` is of a `Class`, then the link will lead to an object of the class `Class`

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
        "id": 1234,
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
        { "rel": [ "about" ], "href": "/v0/courses/daw/classes/1920v" }
    ]
} 
```

## Schedule
A schedule represents a timeframe, in which a set of `Period`s repeat on a weekly or monthly basis. A `Period` has a title, a day of the week or the month, a starting time(hh:mm) and an ending time(hh:mm).
Most common example would be of a school subject schedule. The start date and end date would be the start and end, respectively, of the school term, and the periods would describe on which weekdays and at what time there is class.
By definition a `Schedule` is not an `Event`, however it will be treated as such.

__Properties:__
- id: the unique identifier of this `Schedule`
  - Type: integer
- title: title of the `Schedule`
  - Type: text
  - e.g. "DAW-s1920v Schedule"
- description: description of the `Schedule`
  - Type: text
  - e.g. "Lecture schedule of the DAW-s1920v class."
- start_date: starting date of the schedule
  - Type: date
  - e.g. 19/03/2020
- end_date: ending date of the schedule
  - Type: date
  - e.g. 15/06/2020
  - Notes: 
    - the end_date should be after start_date
    - the gap between start_date and end_date does not need to be longer than a week
- periods: collection of the different periods of the `Schedule`
  - a `Period` object is comprised of:
    - type: [weekly | monthly] - whether it repeats on a monthly or weekly basis
    - day: the day of the week(1-7) or month(1-31) the period takes place in
      - e.g. weekly: 6, monthly: 25
    - start_time: the starting time of the period in hh:mm
      - e.g. 10:30
    - end_time: the ending time of the period in hh:mm
      - e.g. 13:00
    - title: title of the `Period`

__Link Relations:__
- self
- about: the context of the schedule
  - e.g. If the `Schedule` is of a `ClassSection`, then the link will lead to an object of the class-section class

__Example__
- Request
```
GET /v0/courses/daw/classes/s1920v/sections/61D/events HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "schedule" ],
    "properties": {
        "id": 45678,
        "title": "DAW-s1920v Schedule",
        "description": "Lecture schedule of the DAW-s1920v class.",
        "start_date": "26/02/2020",
        "end_date": "15/06/2020",
        "periods": [
            {
                "type": "weekly",
                "day": "2",
                "start_time": "11:00",
                "end_time": "12:30",
                "title": "Aula teórica"
            },
            {
                "type": "weekly",
                "weekday": "5",
                "start_time": "11:00",
                "end_time": "14:00",
                "title": "Aula prática"
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
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/sections/61D/events/45678" },
        { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v/sections/61D" }
    ]
} 
```

## Task

An `Event` that lacks a `start_date` and is coupled with some sort of delivery. For example, a work assignment at school.

__Properties:__
- id: the unique identifier of this `Task`
  - Type: integer
- title: title of the `Task`
  - Type: text
  - e.g. "DAW 1st Exercise Series"
- description: description of the `Task`
  - Type: text
  - e.g. "DAW 1st Exercise Series"
- end_date: deadline for the `Task`
  - Type: date
  - e.g. 30/04/2020

__Link Relations:__
- self
- service-doc: document describing what is to be done and how to deliver it to complete the `Task`
- about: the context of the `Task`
  - e.g. If the `Event` is of a `Class`, then the link will lead to an object of the class `Class`

__Example__
- Request
```
GET /v0/courses/daw/classes/s1920v/events/123490 HTTP/1.1
Host: i-on.pt
```
- Response
```json
{
    "class": [ "task" ],
    "properties": { 
        "id": 123490,
        "title": "DAW 1st Exercise Series",
        "description": "DAW-s1920v 1st Exercise Series",
        "end_date": "30-04-2020 23:59"
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
        { "rel": [ "self" ], "href": "/v0/courses/daw/classes/1920v/events/123490" },
        { "rel": [ "service-doc" ], "href": "/v0/courses/daw/classes/1920v/docs/primeira-serie" },
        { "rel": [ "about" ], "href": "/v0/courses/daw/classes/1920v" }
    ]
} 
```


## Event-list

A collection of `Event`s.

__Properties:__
- size: the size of the collection.
  - Type: integer
  - e.g. 2

__Actions:__
- search: search the collection using the appropriate query parameters
  - query parameters:
    - startBefore: filters `Event`s that have a `start_date` later than specified
    - startAfter: filters `Event`s that have a `start_date` earlier than specified
    - endBefore: filters `Event`s that have a `end_date` later than specified
    - endAfter: filters `Event`s that have a `end_date` earlier than specified
    - title: filters `Event`s that do not have a matching title

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
        "size": 3
    },
    "entities": [
        {
            "class": [ "task" ],
            "rel": [ "item" ],
            "properties": {
                "id": 123490,
                "title": "DAW 1st Exercise Series",
                "description": "DAW-s1920v 1st Exercise Series",
                "end_date": "30-04-2020 23:59"
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/1920v/events/123490" },
                { "rel": [ "service-doc" ], "href": "/v0/courses/daw/classes/1920v/docs/primeira-serie" }
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
        },
        {
            "class": [ "schedule" ],
            "rel": [ "item" ],
            "properties": {
                "id": 45678,
                "title": "DAW-s1920v Schedule",
                "description": "Lecture schedule of the DAW-s1920v class.",
                "start_date": "26/02/2020",
                "end_date": "15/06/2020",
                "periods": [
                    {
                        "type": "weekly",
                        "day": "2",
                        "start_time": "11:00",
                        "end_time": "12:30",
                        "title": "Aula teórica"
                    },
                    {
                        "type": "weekly",
                        "weekday": "5",
                        "start_time": "11:00",
                        "end_time": "14:00",
                        "title": "Aula prática"
                    }
                ]
            },
            "links": [
                { "rel": [ "self" ], "href": "/v0/courses/daw/classes/s1920v/sections/61D/events/45678" },
                { "rel": [ "about" ], "href": "/v0/courses/daw/classes/s1920v/sections/61D" }
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
