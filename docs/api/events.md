# `Event`
An `Event` describes an occurrence within a given time frame, such as an exam, a meeting or an appointment. 

## Properties
* `id`: the unique identifier of this `Event`
  - type: **number**
  - e.g. 38427

* `title`: title of the `Event`
  - type: **text**
  - e.g. "WAD 1st Exam"

* `description`: description of the `Event`
  - type: **text**
  - e.g. "First exam for the WAD-s1920v class. Students are free to bring a cheat sheet of only 1 page, written by hand."

* `start_date`: starting date of the `Event`
  - type: **date**
  - e.g. 19/03/2020

* `end_date`: ending date of the `Event`
  - the `end_date` should be after `start_date`
  - type: **date**
  - e.g. 30/04/2020

## Link Relations
An event representation:
* *must* include a link to its context, using the `self` link relation

## Example representation
```json
{
  "class": [ "event" ],
  "properties": { 
    "id": 1234,
    "title": "WAD 1st Exam",
    "description": "First exam of the WAD course during the semester 1920v",
    "start_date": "19-06-2020 14:00",
    "end_date": "19-06-2020 16:30"
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ],
      "properties": {
        "class_id": "wad-s1920v",
        "term_id": "s1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" },
        { "rel": [ "term" ], "href": "/v0/terms/s1920v" },
        { "rel": [ "course"], "href": "/v0/courses/wad" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v/events/1234" },
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v" }
  ]
} 
```

# `Schedule`
A schedule represents a time frame, in which a set of `Period`s repeat on a weekly or monthly basis. A `Period` has a title, a day of the week or the month, a starting time(hh:mm) and an ending time(hh:mm).
Most common example would be of a school subject schedule. The start date and end date would be the start and end, respectively, of the school term, and the periods would describe on which weekdays and at what time there is class.
By definition a `Schedule` is not an `Event`, however it will be treated as such.

## Properties
* `id`: the unique identifier of this `Schedule`
  - type: **number**
  - e.g. 332

* `title`: title of the `Schedule`
  - type: **text**
  - e.g. "WAD-s1920v Schedule"

* `description`: description of the `Schedule`
  - type: **text**
  - e.g. "Lecture schedule of the WAD-s1920v class."

* `start_date`: starting date of the schedule
  - type: **date**
  - e.g. 19/03/2020

* `end_date`: ending date of the schedule
  - the `end_date` should be after `start_date`
  - the gap between `start_date` and `end_date` does not need to be longer than a week
  - type: **date**
  - e.g. 15/06/2020

* periods: collection of the different periods of the `Schedule`
  - a `Period` object is comprised of:
  - type: [weekly | monthly] - whether it repeats on a monthly or weekly basis
  - day: the day of the week(1-7) or month(1-31) the period takes place in
    - e.g. weekly: 6, monthly: 25
  - `start_time`: the starting time of the period in hh:mm
    - e.g. 10:30
  - `end_time`: the ending time of the period in hh:mm
    - e.g. 13:00
  - `title`: title of the `Period`

## Link Relations
* `self`: the context of the schedule

## Example representation
```json
{
  "class": [ "schedule" ],
  "properties": {
    "id": 45678,
    "title": "WAD-s1920v Schedule",
    "description": "Lecture schedule of the WAD-s1920v class.",
    "start_date": "26/02/2020",
    "end_date": "15/06/2020",
    "periods": [
      {
        "type": "weekly",
        "day": "2",
        "start_time": "11:00",
        "end_time": "12:30",
        "title": "Lab"
      },
      {
        "type": "weekly",
        "weekday": "5",
        "start_time": "11:00",
        "end_time": "14:00",
        "title": "Lab"
      }
    ]
  },
  "entities": [
    {
      "class": [ "class", "section" ],
      "rel": [ "/rel/class-section" ],
      "properties": {
        "id": "61D",
        "lecturer": 1010
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" },
        { "rel": [ "related" ], "href": "/v0/lecturers/1010" }
      ]
    }
  ],
  "actions": [],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D/events/45678" },
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D" }
  ]
} 
```

# `Task`

An `Event` that lacks a `start_date` and is coupled with some sort of delivery. For example, a work assignment at school.

## Properties
* `id`: the unique identifier of this `Task`
  - type: **number**

* `title`: title of the `Task`
  - type: **text**
  - e.g. "WAD 1st Exercise Series"

* `description`: description of the `Task`
  - type: **text**
  - e.g. "WAD 1st Exercise Series"

* `end_date`: deadline for the `Task`
  - type: **date**
  - e.g. 30/04/2020

## Link Relations
A task representation:
* *must* include a link to its context, using the `self` link relation
* *may* include links to documents that describe what has to be done and what to deliver to complete the `Task`, using the `/rel/service-doc` link relation

## Example representation
```json
{
  "class": [ "task" ],
  "properties": { 
    "id": 123490,
    "title": "WAD 1st Series of Exercises",
    "description": "WAD-s1920v 1st Series of Exercises",
    "end_date": "30-04-2020 23:59"
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rels/class" ],
      "properties": {
        "class_id": "wad-s1920v",
        "term_id": "s1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" },
        { "rel": [ "term" ], "href": "/v0/terms/s1920v" },
        { "rel": [ "course"], "href": "/v0/courses/wad" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v/events/123490" },
    { "rel": [ "service-doc" ], "href": "/v0/courses/wad/classes/1920v/docs/first-series" },
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v" }
  ]
} 
```

# `Event Collection`
A collection of `Event`s.

## Properties
* size: the size of the collection.
  - type: **number**
  - e.g. 2

## Link relations
An event collection's representation:
* *must* include a link to its context, using the `self` link relation

## Actions
An event collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for the collection's items.
  - safe
  - templated

## Fields
The `search` action allows the following parameters:
* `startBefore`: filters `Event`s that have a `start_date` later than specified

* `startAfter`: filters `Event`s that have a `start_date` earlier than specified

* `endBefore`: filters `Event`s that have a `end_date` later than specified

* `endAfter`: filters `Event`s that have a `end_date` earlier than specified

* `title`: filters `Event`s that do not have a matching title

## Example representation
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
        "title": "WAD 1st Series of Exercises",
        "description": "WAD-s1920v 1st Series of Exercises",
        "end_date": "30-04-2020 23:59"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v/events/123490" },
        { "rel": [ "service-doc" ], "href": "/v0/courses/wad/classes/1920v/docs/primeira-serie" }
      ]
    },
    {
      "class": [ "event" ],
      "rel": [ "item" ],
      "properties": {
        "event_id": 1235,
        "title": "WAD 2nd Exam",
        "description": "Second exam of the WAD course during the semester 1920v",
        "start_date": "30-06-2020 14:00",
        "end_date": "30-06-2020 16:30"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events/1235" },
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" }
      ]
    },
    {
      "class": [ "schedule" ],
      "rel": [ "item" ],
      "properties": {
        "id": 45678,
        "title": "WAD-s1920v Schedule",
        "description": "Lecture schedule of the WAD-s1920v class.",
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
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D/events/45678" },
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Procurar",
      "method": "GET",
      "href": "/v0/courses/wad/classes/s1920v/events{?startBefore,startAfter,endBefore,endAfter,title}",
      "isTemplated": true,
      "type": "application/x-www-form-urlencoded",
      "fields": [
        { "name": "startBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "startAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "title", "type": "text", "class": "https://example.org/param/free-text-query" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events?page=1" },
    { "rel": [ "next" ], "href": "/v0/courses/wad/classes/s1920v/events?page=2" },
    { "rel": [ "previous" ], "href": "/v0/courses/wad/classes/s1920v/events?page=0" }
  ]
} 
```
