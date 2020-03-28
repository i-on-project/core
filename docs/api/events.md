# iCalendar
All types are iCalendar([RFC5545](https://tools.ietf.org/html/rfc5545) and [RFC7986](https://tools.ietf.org/html/rfc7986)) types: `Event`; `Journal`; `Todo`.
iCalendar and [jCal](https://tools.ietf.org/html/rfc7265) formats are supported and can be obtained by using `text/calendar` or `application/calendar+json`, respectively, in the `Accept` header of requests. If the request made has `application/vdn.siren+json` on the `Accept` header then a in-house representation will be used. This representation still holds the same information as jCal, it simply is displayed in what the team considers a more convenient format.

iCalendar components(`Event`, `Journal` and `Todo`) support sub-components or inner components, however, the ones being used do not allow them selves to be nested in other components, which would mean that there would never exist any sub-components or inner components. As a consequence, only `Calendar` objects have the `subComponents` property.

__Not all properties mentioned in the RFC are supported by this API, only the ones mentioned below. All properties parameters are supported.__

# Date Format
Date formatting is done according to [RFC3339](https://tools.ietf.org/html/rfc3339) which is an Internet profile of the [ISO8601](https://www.iso.org/iso-8601-date-and-time-format.html).

# Date Intervals
All date intervals are inclusive-exclusive, meaning that start and creation dates are included in the interval and due or end dates are excluded from it.
For example, a `Todo` with a created date of `2020-03-19T14:00:00Z` and a due date of `2020-04-20T00:00:00Z` would mean that the last second you could complete the `Todo` would be at 23:59:59 of the day 2020-04-19.

# Object format
All [iCalendar](#icalendar) objects and components will have a `type` property to distinguish its kind. Each of these objects has a `properties` property that is an object that has all of its [properties](https://tools.ietf.org/html/rfc5545#section-3.5). Each individual property will then have a `parameters` and `value` property where its parameters and value are respectively specified.

Multi-valued property values use arrays, but their `type` value is as if the value was not in an array. An array with a single value is equivalent to that same value without the array.
Some properties can occur multiple times. In those cases the property value will not be an object but an array of objects.

The `categories` property of the `Event` in the example demonstrates a reoccuring property and mutli-valued values.

## Example
```json
{
  "type": "calendar",
  "properties": {
    "prodid": {
      "parameters": {},
      "value": "/v0/courses/wad/classes/s1920v/sections/61D"
    },
    "version": {
      "parameters": {},
      "value": "2.0"
    }
  },
  "subComponents": [
    {
      "type": "event",
      "properties": {
        "uid": {
          "parameters": {},
          "value": 45678
        },
        "summary": {
          "parameters": {},
          "value": "Theory Class WAD-s1920v"
        },
        "description": {
          "parameters": {},
          "value": "Theory Class of the WAD-s1920v class."
        },
        "categories": [
          {
            "parameters": {},
            "value": ["Lecture", "Theory Class"],
          },
          {
            "parameters": {},
            "value": "WAD",
          }
        ],
        "dtstamp": {
          "parameters": {},
          "value": "2020-02-10T10:34:24Z"
        },
        "dtstart": {
          "parameters": {},
          "value": "2020-03-19T11:00:00Z"
        },
        "duration": {
          "parameters": {},
          "value": "PT03H00M00S"
        },
        "rrule": {
          "parameters": {},
          "value": {
            "freq": "WEEKLY",
            "until": "2020-06-10T00:00:00Z",
            "byDay": "TH"
          }
        }
      }
    }
  ]
}
```

# `Calendar`
A calendar, as described [here](https://icalendar.org/iCalendar-RFC-5545/3-4-icalendar-object.html), represents a component container. Each individual object of the domain has its own calendar.

## Properties
* `version`: the unique version of [iCalendar](#icalendar) being used
  - type: **text**
  - e.g. "2.0"

* `prodid`: id of the creator of the calendar. In this domain it could be the identifier of a `Class`, `ClassSection`, etc.
  - type: **text**
  - e.g. "/v0/courses/wad/classes/s1920v/sections/61D"
## Actions
An event collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for the collection's items.
  - safe
  - templated

### Fields
The `search` action allows the following parameters:
* `type`: filters **components** that don't have the specified `type`

* `startBefore`: filters **components** that have a `dtstart` later than specified

* `startAfter`: filters **components** that have a `dtstart` earlier than specified

* `endBefore`: filters **components** that have a `dtend` later than specified

* `endAfter`: filters **components** that have a `dtend` earlier than specified

* `summary`: filters **components** that do not have a matching `summary`

The `batch-delete` action allows the following parameters:
* `type`: delete all components whose `type` matches the one indicated
  - e.g. /v0/courses/wad/classes/s1920v/sections/61D/calendar?type=event, will delete all events

## Link Relations
A `Calendar` representation:
* *must* include a link to its context, using the `self` link relation
* *must* include a link to its creation context, using the `about` link relation
  - e.g. if a `Calendar` is created for a specific `ClassSection` then the `about` link relation would refer to that same `ClassSection`

## Example representation
```json
{
  "class": [ "calendar" ],
  "properties": {
    "type": "calendar",
    "properties": {
      "prodid": {
        "parameters": {},
        "value": "/v0/courses/wad/classes/s1920v/sections/61D"
      },
      "version": {
        "parameters": {},
        "value": "2.0"
      }
    },
    "subComponents": [
      {
        "type": "event",
        "properties": {
          "uid": {
            "parameters": {},
            "value": 45678
          },
          "summary": {
            "parameters": {},
            "value": "Theory Class WAD-s1920v"
          },
          "description": {
            "parameters": {},
            "value": "Theory Class of the WAD-s1920v class."
          },
          "categories": {
            "parameters": {},
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "parameters": {},
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "parameters": {},
            "value": "2020-03-19T11:00:00Z"
          },
          "duration": {
            "parameters": {},
            "value": "PT03H00M00S"
          },
          "rrule": {
            "parameters": {},
            "value": {
              "freq": "WEEKLY",
              "until": "2020-06-10T00:00:00Z",
              "byDay": [ "TH" ]
            }
          }
        }
      },
      {
        "type": "event",
        "properties": {
          "uid": {
            "parameters": {},
            "value": 45679
          },
          "summary": {
            "parameters": {},
            "value": "Theory Class WAD-s1920v"
          },
          "description": {
            "parameters": {},
            "value": "Theory Class of the WAD-s1920v class."
          },
          "categories": {
            "parameters": {},
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "parameters": {},
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "parameters": {},
            "value": "2020-03-16T11:00:00Z"
          },
          "duration": {
            "parameters": {},
            "value": "PT01H30M00S"
          },
          "rrule": {
            "parameters": {},
            "value": {
              "freq": "WEEKLY",
              "until": "2020-06-10T00:00:00Z",
              "byDay": [ "MO" ]
            }
          }
        }
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
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "summary": "Search components",
      "method": "GET",
      "href": "/v0/courses/wad/classes/s1920v/components{?type,startBefore,startAfter,endBefore,endAfter,summary}",
      "isTemplated": true,
      "type": "application/x-www-form-urlencoded",
      "fields": [
        { "name": "type", "type": "text", "class": "https://example.org/param/free-text-query" },
        { "name": "startBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "startAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endBefore", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "endAfter", "type": "date", "class": "https://example.org/param/date-query" },
        { "name": "summary", "type": "text", "class": "https://example.org/param/free-text-query" }
      ]
    },
    {
      "name": "add-item",
      "title": "Add Item",
      "method": "POST",
      "href": "/v0/courses/wad/classes/s1920v/components",
      "isTemplated": false,
      "type": "application/json",
      "fields": [ ]
    },
    {
      "name": "batch-delete",
      "title": "Delete multiple items",
      "method": "DELETE",
      "isTemplated": true,
      "href": "/v0/courses/wad/classes/s1920v/components{?type}",
      "fields": [
        { "name": "type", "type": "text", "class": "https://example.org/param/free-text-query" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D/calendar" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v/sections/61D" }
  ]
} 
```

# `Event`
An `Event`, as described [here](https://tools.ietf.org/html/rfc5545#section-3.6.1), represents an occurrence within a given time frame, such as an exam, a meeting or an appointment. It can also represent a repeating occurrence if the [`rrule`](https://tools.ietf.org/html/rfc5545#section-3.8.5.3) property is specified. For an example of a recurring event check the `Calendar` [example](#example-representation)

## Properties
* `uid`: the unique identifier of this `Event`
  - [link](https://tools.ietf.org/html/rfc7986#section-5.3)
  - type: [**integer**](https://tools.ietf.org/html/rfc5545#section-3.3.8)
  - e.g. 38427

* `summary`: summary of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.12)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "WAD 1st Exam"

* `description`: description of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.5)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "First exam for the WAD-s1920v class. Students are free to bring a cheat sheet of only 1 page, written by hand."

* `dtstamp`: date of the last modification of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.2)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `created`: date of creation of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.1)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `dtstart`: starting date of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.4)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
    - alternate types:
      - [**date**](https://tools.ietf.org/html/rfc5545#section-3.3.4)
  - e.g. 2020-03-19T14:00:00Z

* `dtend`: ending date of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.2)
  - only one of `duration` or `dtend` can be present
  - the `endDate` should be after `startDate`
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
    - alternate types:
      - [**date**](https://tools.ietf.org/html/rfc5545#section-3.3.4)
        - if this data type is used for `dtend` then it **must** be used for `dtstart` aswell
  - e.g. 2020-03-19T16:30:00Z

* `duration`: duration of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.5)
  - only one of `duration` or `dtend` can be present
  - type: [**duration**](https://tools.ietf.org/html/rfc5545#section-3.3.6)
  - e.g. "P45DT23H12M56S"

* `categories`: categories of the `Event`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.2)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - supports multiple values by using an array
  - e.g. "Exam", "Lecture", ["Evaluation", "Exam"]

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
        "value": 1234
      },
      "summary": {
        "parameters": {},
        "value": "WAD 1st Exam"
      },
      "description": {
        "parameters": {},
        "value": "First exam of the WAD course during the semester 1920v"
      },
      "categories": {
        "parameters": {},
        "value": "Exam",
      },
      "created": {
        "parameters": {},
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstamp": {
        "parameters": {},
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstart": {
        "parameters": {},
        "value": "2020-03-19T14:00:00Z"
      },
      "dtend": {
        "parameters": {},
        "value": "2020-03-19T16:30:00Z"
      },
    }
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
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/calendar/components/1234" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
  ]
}
```

# `Todo`
A calendar component designed to represent an assignment or something that requires action, e.g. a work assignment at school. It lacks a starting date because the ability for it to be done is not aquired after the `Todo` is created. The instant it is created is the instant it is started.

## Properties
* `uid`: the unique identifier of this `Todo`
  - [link](https://tools.ietf.org/html/rfc7986#section-5.3)
  - type: [**integer**](https://tools.ietf.org/html/rfc5545#section-3.3.8)
  - e.g. 38427

* `summary`: summary of the `Todo`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.12)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "WAD 1st Series"

* `description`: description of the `Todo`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.5)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "First series of exercises for the WAD course during the semester 1920v"

* `attach`: attachment related that could, for example, be related to the requirements or objectives of the `Todo`
  - this property is optional
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.1)
  - type: [**uri**](https://tools.ietf.org/html/rfc5545#section-3.3.13)
  - e.g. "https://i-on.pt/v0/courses/wad/classes/1920v/documents/primeira-serie.pdf"

* `dtstamp`: date of the last modification of the `Todo`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.2)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `created`: date of creation of the `Todo`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.1)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `due`: defines the date and time that the `Todo` is expected to be completed
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.3)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
    - alternate types:
      - [**date**](https://tools.ietf.org/html/rfc5545#section-3.3.4)
  - e.g. 2020-03-19T00:00:00Z

* `categories`: categories of the `Todo`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.2)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - supports multiple values by using an array
  - e.g. "Math", "Lecture", ["Evaluation", "Assignment", "Cyber Security"]

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
    "uid": {
      "parameters": {},
      "value": 123490
    },
    "summary": {
      "parameters": {},
      "value": "WAD 1st Series"
    },
    "description": {
      "parameters": {},
      "value": "First series of exercises for the WAD course during the semester 1920v"
    },
    "attachment": {
      "parameters": {},
      "value": "https://api.i-on.pt/v0/courses/wad/classes/1920v/documents/primeira-serie.pdf"
    },
    "categories": {
      "parameters": {},
      "value": ["Evaluation", "Assignment", "Web App Development"],
    },
    "created": {
      "parameters": {},
      "value": "2020-02-10T10:34:20Z"
    },
    "dtstamp": {
      "parameters": {},
      "value": "2020-02-12T12:24:50Z"
    },
    "due": {
      "parameters": {},
      "value": "2020-03-19T00:00:00Z"
    },
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
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/calendar/components/123490" },
    { "rel": [ "service-doc" ], "href": "/v0/courses/wad/classes/s1920v/docs/first-series" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" }
  ]
} 
```

# `Journal`
A calendar component meant to associate text with a given time.
For example, a synopsis of a lecture.

## Properties
* `uid`: the unique identifier of this `Journal`
  - [link](https://tools.ietf.org/html/rfc7986#section-5.3)
  - type: [**integer**](https://tools.ietf.org/html/rfc5545#section-3.3.8)
  - e.g. 38427

* `summary`: summary of the `Journal`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.12)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "WAD 1st Series"

* `description`: description of the `Journal`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.5)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - e.g. "First series of exercises for the WAD course during the semester 1920v"

* `attach`: attachment that related to the content of the `Journal`
  - this property is optional
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.1)
  - type: [**uri**](https://tools.ietf.org/html/rfc5545#section-3.3.13)
  - e.g. "https://i-on.pt/v0/courses/wad/classes/1920v/documents/primeira-serie.pdf"

* `dtstamp`: date of the last modification of the `Journal`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.2)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `dtstart`: date that the `Journal` entry is refering to
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.2.4)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
    - alternate types:
      - [**date**](https://tools.ietf.org/html/rfc5545#section-3.3.4)
  - e.g. 2020-03-19T14:00:00Z

* `created`: date of creation of the `Journal`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.7.1)
  - type: [**date-time**](https://tools.ietf.org/html/rfc5545#section-3.3.5)
  - e.g. 2020-03-19T14:00:00Z

* `categories`: categories of the `Journal`
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.1.2)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)
  - supports multiple values by using an array
  - e.g. "Math", "Lecture", ["Evaluation", "Assignment", "Cyber Security"]

* `relatedTo`: Unique identifiers of other components the `Journal` entry is related to
  - [link](https://tools.ietf.org/html/rfc5545#section-3.8.4.5)
  - type: [**text**](https://tools.ietf.org/html/rfc5545#section-3.3.11)

## Link Relations
A Todo representation:
* *must* include a link to its context, using the `self` link relation
* *must* include a link to its creation context, using the `about` link relation
  - e.g. if a `Journal` is created for a specific `ClassSection` then the `about` link relation would refer to that same `ClassSection`
* *may* include links to the other components it refers to with the `relatedTo` property using the `related` link relation

## Example representation
```json
{
  "class": [ "journal" ],
  "properties": { 
    "uid": {
      "parameters": {},
      "value": 123497
    },
    "summary": {
      "parameters": {},
      "value": "Error in 1st WAD Exam"
    },
    "description": {
      "parameters": {},
      "value": "The first exercise had and ambigous question that resulted in both answers B and C being correct. Therefore both will be awarded full marks."
    },
    "categories": {
      "parameters": {},
      "value": ["Error", "Evaluation", "Exam", "Web App Development"],
    },
    "created": {
      "parameters": {},
      "value": "2020-02-10T10:34:20Z"
    },
    "dtstamp": {
      "parameters": {},
      "value": "2020-02-12T12:24:50Z"
    },
    "dtstart": {
      "parameters": {},
      "value": "2020-03-19T14:00:00Z"
    },
    "relatedTo": [
      {
        "parameters": {},
        "value": "/v0/courses/wad/classes/s1920v/calendar/components/1234"
      },
      {
        "parameters": {
          "reltype": "SIBLING"
        },
        "value": "/v0/courses/wad/classes/s1920v/calendar/components/123485"
      }
    ],
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
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes/s1920v/calendar/components/123497" },
    { "rel": [ "about" ], "href": "/v0/courses/wad/classes/s1920v" },
    { "rel": [ "related" ], "href": [ "/v0/courses/wad/classes/s1920v/calendar/components/1234", "/v0/courses/wad/classes/s1920v/calendar/components/123485" ] }
  ]
} 
```