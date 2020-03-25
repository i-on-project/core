# iCalendar
All types are iCalendar([RFC5545](https://tools.ietf.org/html/rfc5545) and [RFC7986](https://tools.ietf.org/html/rfc7986)) types: `Event`; `Journal`; `Todo`.
iCalendar and [jCal](https://tools.ietf.org/html/rfc7265) formats are supported and can be obtained by using `text/calendar` or `application/calendar+json`, respectively, in the `Accept` header of requests. If the request made has `application/vdn.siren+json` on the `Accept` header then a in-house representation will be used. This representation still holds the same information as jCal, it simply is displayed in what the team considers a more convient format.

iCalendar components support sub-components, however, the components being used(`Event`, `Journal` and `Todo`) cannot be nested in other components so there would never exist any subcomponents. As a consequence, the `subComponents` property of the objects has been ommitted.

# Date Format
Date formatting is done according to [RFC3339](https://tools.ietf.org/html/rfc3339) which is an Internet profile of the [ISO8601](https://www.iso.org/iso-8601-date-and-time-format.html).

# `Event`
An `Event`, as described [here](https://tools.ietf.org/html/rfc5545#section-3.6.1) describes an occurrence within a given time frame, such as an exam, a meeting or an appointment. It can also represent a repeating occurrence if the [`rrule`](https://tools.ietf.org/html/rfc5545#section-3.8.5.3) property is specified. __Not all properties mentioned in the RFC are supported by this API, only the ones mentioned below. All properties parameters are supported.__

## Properties
* `uid`: the unique identifier of this `Event`
  - [link](https://tools.ietf.org/html/rfc7986#section-5.3)
  - type: **number**
  - e.g. 38427

* `summary`: summary of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.12)
  - type: **text**
  - e.g. "WAD 1st Exam"

* `description`: description of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.5)
  - type: **text**
  - e.g. "First exam for the WAD-s1920v class. Students are free to bring a cheat sheet of only 1 page, written by hand."

* `dtstamp`: date of the last modification of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.2)
  - type: **date-time**
  - e.g. 2020-03-19T14:00:00Z

* `created`: date of creation of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.1)
  - type: **date-time**
  - e.g. 2020-03-19T14:00:00Z

* `dtstart`: starting date of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.4)
  - type: **date-time**
  - e.g. 2020-03-19T14:00:00Z

* `dtend`: ending date of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.2)
  - the `endDate` should be after `startDate`
  - type: **date-time**
  - e.g. 2020-03-19T16:30:00Z

* `categories`: categories of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.2)
  - type: **text**
  - e.g. "Exam", "Lecture"

* `rrule`: recurrency rule of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.5.3)
  - type: [**recur**](https://tools.ietf.org/html/rfc5545#section-3.3.10)

## Link Relations
An event representation:
* *must* include a link to its context, using the `self` link relation
* *must* include a link to its creation context, using the `about` link relation
  - e.g. if an `Event` is created for a specific `Class` then the `about` link relation would refer to that same `Class`

## Example representation
MIME type: __application/vdn.siren+json__
```json
{
  "class": [ "event" ],
  "properties": {
    "type": "event",
    "properties": {
      "uid": {
        "parameters": {},
        "type": "integer",
        "value": 1234
      },
      "summary": {
        "parameters": {},
        "type": "text",
        "value": "WAD 1st Exam"
      },
      "description": {
        "parameters": {},
        "type": "text",
        "value": "First exam of the WAD course during the semester 1920v"
      },
      "categories": {
        "parameters": {},
        "type": "text",
        "value": "Exam",
      },
      "created": {
        "parameters": {},
        "type": "date-time",
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstamp": {
        "parameters": {},
        "type": "date-time",
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstart": {
        "parameters": {},
        "type": "date-time",
        "value": "2020-03-19T14:00:00Z"
      },
      "dtend": {
        "parameters": {},
        "type": "date-time",
        "value": "2020-03-19T16:30:00Z"
      },
    },
    "subComponents": []
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ],
      "properties": {
        "classId": "wad-s1920v",
        "termId": "s1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" },
        { "rel": [ "term" ], "href": "/v0/terms/s1920v" },
        { "rel": [ "course"], "href": "/v0/courses/wad" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events/1234" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
  ]
}
```

# `Schedule`
A schedule represents a time frame, in which a set of `Period`s repeat on a weekly or monthly basis. A `Period` has a summary, a day of the week or the month, a starting time(hh:mm) and an ending time(hh:mm).
Most common example would be of a school subject schedule. The start date and end date would be the start and end, respectively, of the school term, and the periods would describe on which weekdays and at what time there is class.
By definition a `Schedule` is not an `Event`, however it will be treated as such.

## Properties
* `uid`: the unique identifier of this `Schedule`
  - type: **number**
  - e.g. 332

* `summary`: summary of the `Schedule`
  - type: **text**
  - e.g. "WAD-s1920v Schedule"

* `description`: description of the `Schedule`
  - type: **text**
  - e.g. "Lecture schedule of the WAD-s1920v class."

* `creationDate`: date of creation of the `Schedule`
  - type: **date**
  - e.g. 2020-03-19T14:00:00Z

* `startDate`: starting date of the schedule
  - type: **date**
  - e.g. 2020-03-19T00:00:00Z

* `endDate`: ending date of the schedule
  - the `endDate` should be after `startDate`
  - the gap between `startDate` and `endDate` does not need to be longer than a week
  - type: **date**
  - e.g. 2020-06-15T00:00:00Z

* periods: collection of the different periods of the `Schedule`
  - a `Period` object is comprised of:
  - `frequency`: [weekly | monthly] - whether it repeats on a monthly or weekly basis
  - `day`: the day of the week(1-7) or month(1-31) the period takes place in
    - e.g. weekly: 6, monthly: 25
  - `startTime`: the starting time of the period in hh:mm
    - e.g. 10:30
  - `endTime`: the ending time of the period in hh:mm
    - e.g. 13:00
  - `summary`: summary of the `Period`

## Link Relations
A `Schedule` representation:
* *must* include a link to its context, using the `self` link relation
* *must* include a link to its creation context, using the `about` link relation
  - e.g. if a `Schedule` is created for a specific `ClassSection` then the `about` link relation would refer to that same `ClassSection`

## Example representation
```json
{
  "class": [ "schedule" ],
  "properties": {
    "type": "calendar",
    "properties": {
      "prodid": {
        "parameters": {},
        "type": "text",
        "value": "/v0/courses/wad/classes/s1920v/sections/61D"
      },
      "version": {
        "parameters": {},
        "type": "text",
        "value": "2.0"
      }
    },
    "subComponents": [
      {
        "type": "event",
        "properties": {
          "uid": {
            "parameters": {},
            "type": "integer",
            "value": 45678
          },
          "summary": {
            "parameters": {},
            "type": "text",
            "value": "Theory Class WAD-s1920v"
          },
          "description": {
            "parameters": {},
            "type": "text",
            "value": "Theory Class of the WAD-s1920v class."
          },
          "categories": {
            "parameters": {},
            "type": "text",
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "parameters": {},
            "type": "date-time",
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "parameters": {},
            "type": "date-time",
            "value": "2020-03-19T11:00:00Z"
          },
          "duration": {
            "parameters": {},
            "type": "dur-time",
            "value": "PT03H00M00S"
          },
          "rrule": {
            "parameters": {},
            "type": "recur",
            "value": {
              "freq": "WEEKLY",
              "until": "2020-06-10T00:00:00Z",
              "byDay": [ "TH" ]
            }
          }
        },
        "subComponents": []
      },
      {
        "type": "event",
        "properties": {
          "uid": {
            "parameters": {},
            "type": "integer",
            "value": 45679
          },
          "summary": {
            "parameters": {},
            "type": "text",
            "value": "Theory Class WAD-s1920v"
          },
          "description": {
            "parameters": {},
            "type": "text",
            "value": "Theory Class of the WAD-s1920v class."
          },
          "categories": {
            "parameters": {},
            "type": "text",
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "parameters": {},
            "type": "date-time",
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "parameters": {},
            "type": "date-time",
            "value": "2020-03-16T11:00:00Z"
          },
          "duration": {
            "parameters": {},
            "type": "dur-time",
            "value": "PT01H30M00S"
          },
          "rrule": {
            "parameters": {},
            "type": "recur",
            "value": {
              "freq": "WEEKLY",
              "until": "2020-06-10T00:00:00Z",
              "byDay": [ "MO" ]
            }
          }
        },
        "subComponents": []
      }
    ]
  },
  "entities": [
    {
      "class": [ "class", "section" ],
      "rel": [ "/rel/class-section" ],
      "properties": {
        "uid": "61D",
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
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D/calendar" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D" }
  ]
} 
```

# `Todo`

An `Event` that lacks a `startDate` and is coupled with some sort of delivery. For example, a work assignment at school.

## Properties
* `uid`: the unique identifier of this `Todo`
  - type: **number**

* `summary`: summary of the `Todo`
  - type: **text**
  - e.g. "WAD 1st Exercise Series"

* `description`: description of the `Todo`
  - type: **text**
  - e.g. "WAD 1st Exercise Series"

* `creationDate`: date of creation of the `Schedule`
  - type: **date**
  - e.g. 2020-03-19T14:00:00Z

* `endDate`: due date for the `Todo`
  - type: **date**
  - e.g. 2020-04-30T23:59:59Z

## Link Relations
A Todo representation:
* *must* include a link to its context, using the `self` link relation
* *must* include a link to its creation context, using the `about` link relation
  - e.g. if a `Todo` is created for a specific `ClassSection` then the `about` link relation would refer to that same `ClassSection`
* *may* include links to documents that describe what has to be done and what to deliver to complete the `Todo`, using the `/rel/service-doc` link relation

## Example representation
```json
{
  "class": [ "todo" ],
  "properties": { 
    "uid": 123490,
    "summary": "WAD 1st Series of Exercises",
    "description": "WAD-s1920v 1st Series of Exercises",
    "creationDate": "2020-02-10T10:34:23Z",
    "endDate": "2020-04-30T23:59:59Z"
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rels/class" ],
      "properties": {
        "classId": "wad-s1920v",
        "termId": "s1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" },
        { "rel": [ "term" ], "href": "/v0/terms/s1920v" },
        { "rel": [ "course"], "href": "/v0/courses/wad" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events/123490" },
    { "rel": [ "service-doc" ], "href": "/v0/courses/wad/classes/s1920v/docs/first-series" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
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
* `startBefore`: filters `Event`s that have a `startDate` later than specified

* `startAfter`: filters `Event`s that have a `startDate` earlier than specified

* `endBefore`: filters `Event`s that have a `endDate` later than specified

* `endAfter`: filters `Event`s that have a `endDate` earlier than specified

* `summary`: filters `Event`s that do not have a matching summary

## Example representation
```json
{
  "class": [ "collection", "event" ],
  "properties": { 
    "size": 3
  },
  "entities": [
    {
      "class": [ "todo" ],
      "rel": [ "item" ],
      "properties": {
        "uid": 123490,
        "summary": "WAD 1st Series of Exercises",
        "description": "WAD-s1920v 1st Series of Exercises",
        "endDate": "2020-04-30T23:59:59Z"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events/123490" },
        { "rel": [ "service-doc" ], "href": "/v0/courses/wad/classes/s1920v/docs/primeira-serie" },
        { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
      ]
    },
    {
      "class": [ "event" ],
      "rel": [ "item" ],
      "properties": {
        "uid": 1235,
        "summary": "WAD 2nd Exam",
        "description": "Second exam of the WAD course during the semester 1920v",
        "startDate": "2020-06-30T14:00:00Z",
        "endDate": "2020-06-30T16:30:00Z"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/events/1235" },
        { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
      ]
    },
    {
      "class": [ "schedule" ],
      "rel": [ "item" ],
      "properties": {
        "uid": 45678,
        "summary": "WAD-s1920v Schedule",
        "description": "Lecture schedule of the WAD-s1920v class.",
        "startDate": "2020-02-26T00:00:00Z",
        "endDate": "2020-06-15T23:59:59Z",
        "periods": [
          {
            "type": "weekly",
            "day": "2",
            "startTime": "11:00",
            "endTime": "12:30",
            "summary": "Aula teórica"
          },
          {
            "type": "weekly",
            "weekday": "5",
            "startTime": "11:00",
            "endTime": "14:00",
            "summary": "Aula prática"
          }
        ]
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D/events/45678" },
        { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "summary": "Procurar",
      "method": "GET",
      "href": "/v0/courses/wad/classes/s1920v/events{?startBefore,startAfter,endBefore,endAfter,summary}",
      "isTemplated": true,
      "type": "application/x-www-form-urlencoded",
      "fields": [
        { "name": "startBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "startAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "summary", "type": "text", "class": "https://example.org/param/free-text-query" }
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
