# iCalendar
All types are iCalendar([RFC5545](https://tools.ietf.org/html/rfc5545) and [RFC7986](https://tools.ietf.org/html/rfc7986)) types: `Event`; `Journal`; `Todo`.
iCalendar and [jCal](https://tools.ietf.org/html/rfc7265) formats are supported and can be obtained by using `text/calendar` or `application/calendar+json`, respectively, in the `Accept` header of requests. If the request made has `application/vdn.siren+json` on the `Accept` header then a in-house representation will be used. This representation still holds the same information as jCal, it simply is displayed in what the team considers a more convenient format.

iCalendar components(`Event`, `Journal` and `Todo`) support sub-components or inner components, however, the ones being used do not allow them selves to be nested in other components, which would mean that there would never exist any sub-components or inner components. As a consequence, only `Calendar` objects have the `subComponents` property.

__Not all properties mentioned in the RFC are supported by this API, only the ones mentioned below. All properties parameters are supported.__

# Index
- [Date Format](#date-format)
- [Date Intervals](#date-intervals)
- [Object format](#object-format)
- [Components](#calendar)
  - [Calendar](#calendar)
  - [Event](#event)
  - [Todo](#todo)
  - [Journal](#journal)
- [Properties](#all-properties)
- [Data types](#data-types)

# Date Format
Date formatting is done according to [RFC3339](https://tools.ietf.org/html/rfc3339) which is an Internet profile of the [ISO8601](https://www.iso.org/iso-8601-date-and-time-format.html). Examples can be found [here](https://tools.ietf.org/html/rfc3339#section-5.8).

# Date Intervals
All date intervals are inclusive-exclusive, meaning that start and creation dates are included in the interval and due or end dates are excluded from it.
For example, a `Todo` with a created date of `2020-03-19T14:00:00Z` and a due date of `2020-04-20T00:00:00Z` would mean that the last second you could complete the `Todo` would be at 23:59:59 of the day 2020-04-19.

# Object format
All [iCalendar](#icalendar) objects and components will have a `type` property to distinguish its kind. Each of these objects has a `properties` property that is an object that has all of its [properties](https://tools.ietf.org/html/rfc5545#section-3.5). Each individual property will then have a `parameters` and `value` property where its parameters and value are respectively specified.

Multi-valued property values use arrays, but their `type` value is as if the value was not in an array. An array with a single value is equivalent to that same value without the array.
Some properties can occur multiple times. In those cases the property will not be an object but an array of objects.

The `categories` property of the `Event` in the example demonstrates a reoccuring property and mutli-valued values.

## Example
```json
{
  "type": "calendar",
  "properties": {
    "prodid": {
      "value": "/v0/courses/1/classes/1920v/61D"
    },
    "version": {
      "value": "2.0"
    }
  },
  "subComponents": [
    {
      "type": "event",
      "properties": {
        "uid": {
          "value": "event/45678"
        },
        "summary": {
          "value": "Theory Class WAD-1920v"
        },
        "description": [
          {
            "value": "Theory Class of the WAD-1920v class."
          },
          {
            "parameters": {
              "language": "pt/PT"
            },
            "value": "Aula teórica da turma DAW-1920v."
          }
        ],
        "categories": [
          {
            "value": ["Lecture", "Theory Class"],
          },
          {
            "value": "WAD",
          }
        ],
        "dtstamp": {
          "value": "2020-02-10T10:34:24Z"
        },
        "dtstart": {
          "value": "2020-03-19T11:00:00Z"
        },
        "duration": {
          "value": "PT03H00M00S"
        },
        "rrule": {
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
* [version](#version)
* [prodid](#prodid)

## Actions
An event collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for the collection's items.
  - safe
  - templated

### Fields
The `search` action allows the following parameters:
* `type`: filters **components** that don't have the specified `type`
  * Possible types
    * E - Events  
    * T - Todos
    * J - Journals
  * If multiple types are specified, such as E and T, the search result will have components of the `Event` and `Todo` type. If all types are specified it is the same as not specifying one.

* `startBefore`: filters **components** that have a `dtstart` later than specified

* `startAfter`: filters **components** that have a `dtstart` earlier than specified

* `endBefore`: filters **components** that have a `dtend` later than specified

* `endAfter`: filters **components** that have a `dtend` earlier than specified

* `summary`: filters **components** that do not have a matching `summary`

#### Invalid Date Type
The date used in the `startBefore`, `startAfter`, `endBefore` and `endAfter` must follow the format specified in the [Date Format Section](#date-format).

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
        "value": "/v0/courses/1/classes/1920v/61D"
      },
      "version": {
        "value": "2.0"
      }
    },
    "subComponents": [
      {
        "type": "event",
        "properties": {
          "uid": {
            "value": "event/45678"
          },
          "summary": {
            "value": "Theory Class WAD-1920v"
          },
          "description": {
            "value": "Theory Class of the WAD-1920v class."
          },
          "categories": {
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "value": "2020-03-19T11:00:00Z"
          },
          "duration": {
            "value": "PT03H00M00S"
          },
          "rrule": {
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
            "value": "event/45679"
          },
          "summary": {
            "value": "Theory Class WAD-1920v"
          },
          "description": {
            "value": "Theory Class of the WAD-1920v class."
          },
          "categories": {
            "value": ["Lecture", "Theory Class"],
          },
          "dtstamp": {
            "value": "2020-02-10T10:34:24Z"
          },
          "dtstart": {
            "value": "2020-03-16T11:00:00Z"
          },
          "duration": {
            "value": "PT01H30M00S"
          },
          "rrule": {
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
  "actions": [
    {
      "name": "search",
      "summary": "Search components",
      "method": "GET",
      "href": "/v0/courses/1/classes/1920v/calendar{?type,startBefore,startAfter,endBefore,endAfter,summary}",
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
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v/61D/calendar" },
    { "rel": [ "about" ], "href": "/v0/courses/1/classes/1920v/61D" }
  ]
} 
```

# `Event`
An `Event`, as described [here](https://tools.ietf.org/html/rfc5545#section-3.6.1), represents an occurrence within a given time frame, such as an exam, a meeting or an appointment. It can also represent a repeating occurrence if the [`rrule`](https://tools.ietf.org/html/rfc5545#section-3.8.5.3) property is specified. For an example of a recurring event check the `Calendar` [example](#example-representation)

## Properties
* [uid](#uid)
* [summary](#summary)  
* [description](#description)
* [dtstamp](#dtstamp)
* [created](#created)
* [categories](#categories)
* [dtstart](#dtstart)
* [dtend](#dtend)
* [rrule](#rrule)
  - this property is optional and will only be defined on recurring `Event`s

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
        "value": "event/1234"
      },
      "summary": {
        "value": "WAD 1st Exam"
      },
      "description": {
        "value": "First exam of the WAD course during the 1920v semester"
      },
      "categories": {
        "value": "Exam",
      },
      "created": {
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstamp": {
        "value": "2020-02-10T10:34:20Z"
      },
      "dtstart": {
        "value": "2020-03-19T14:00:00Z"
      },
      "dtend": {
        "value": "2020-03-19T16:30:00Z"
      },
    }
  },
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v/calendar/1234" },
    { "rel": [ "about" ], "href": "/v0/courses/1/classes/1920v" }
  ]
}
```

# `Todo`
A calendar component designed to represent an assignment or something that requires action, e.g. a work assignment at school. It lacks a starting date because the ability for it to be done is not aquired after the `Todo` is created. The instant it is created is the instant it is started.

## Properties
* [uid](#uid)
* [summary](#summary)  
* [description](#description)
* [dtstamp](#dtstamp)
* [created](#created)
* [attach](#attach)
  - this property is optional
* [categories](#categories)
* [due](#due)

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
      "value": "todo/123490"
    },
    "summary": {
      "value": "WAD 1st Series"
    },
    "description": {
      "value": "First series of exercises for the WAD course during the 1920v semester"
    },
    "attachment": {
      "value": "https://github.com/isel-leic-daw/1920v-public/wiki/phase-1"
    },
    "categories": {
      "value": ["Evaluation", "Assignment", "Web App Development"],
    },
    "created": {
      "value": "2020-02-10T10:34:20Z"
    },
    "dtstamp": {
      "value": "2020-02-12T12:24:50Z"
    },
    "due": {
      "value": "2020-03-19T00:00:00Z"
    },
  },
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v/calendar/123490" },
    { "rel": [ "service-doc" ], "href": "https://github.com/isel-leic-daw/1920v-public/wiki/phase-1" },
    { "rel": [ "about" ], "href": "/v0/courses/1/classes/1920v" }
  ]
} 
```

# `Journal`
A calendar component meant to associate text with a given time.
For example, a synopsis of a lecture.

## Properties
* [uid](#uid)
* [summary](#summary)  
* [description](#description)
* [attach](#attach)
  - this property is optional
* [dtstamp](#dtstamp)
* [dtstart](#dtstart)
* [created](#created)
* [categories](#categories)
* [relatedTo](#relatedto)

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
      "value": "journal/123497"
    },
    "summary": {
      "value": "Error in 1st WAD Exam"
    },
    "description": {
      "value": "The first exercise had and ambigous question that resulted in both answers B and C being correct. Therefore both will be awarded full marks."
    },
    "categories": {
      "value": ["Error", "Evaluation", "Exam", "Web App Development"],
    },
    "created": {
      "value": "2020-02-10T10:34:20Z"
    },
    "dtstamp": {
      "value": "2020-02-12T12:24:50Z"
    },
    "dtstart": {
      "value": "2020-03-19T14:00:00Z"
    },
    "relatedTo": [
      {
        "value": "/v0/courses/1/classes/1920v/calendar/1234"
      },
      {
        "parameters": {
          "reltype": "SIBLING"
        },
        "value": "/v0/courses/1/classes/1920v/calendar/123485"
      }
    ],
  },
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v/calendar/123497" },
    { "rel": [ "about" ], "href": "/v0/courses/1/classes/1920v" },
    { "rel": [ "related" ], "href": [ "/v0/courses/1/classes/1920v/calendar/1234", "/v0/courses/1/classes/1920v/calendar/123485" ] }
  ]
} 
```

# All Properties
## `version`
The unique version of [iCalendar](#icalendar) being used

[RFC](https://tools.ietf.org/html/rfc5545#section-3.7.4)
  - type: **text**
  - e.g. "2.0"

## `prodid`
Id of the creator of the calendar. In this domain it could be the identifier of a `Class`, `ClassSection`, etc.

[RFC](https://tools.ietf.org/html/rfc5545#section-3.7.3)
  - type: **text**
  - The text will be the full path to the resource it is identifying
  - e.g. "/v0/courses/1/classes/1920v/1"

## `uid`
The unique identifier of a component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.4.7)
  - type: [**text**](#text)
  - e.g. "event/1234", "todo/1234523", "journal/2"

## `summary`
Summary of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.1.12)
  - type: [**text**](#text)
  - e.g. "WAD 1st Series"

## `description`
Description of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.1.5)
  - type: [**text**](#text)
  - e.g. "First series of exercises for the WAD course during the 1920v semester"

## `attach`

Attachment that related to the content of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.1.1)
  - type: [**uri**](#uri)

## `dtstamp`

Date of the last modification of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.7.2)
  - type: [**date-time**](#datetime)
  - e.g. 2020-03-19T14:00:00Z

## `dtstart`

Starting date

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.2.4)
  - type: [**date-time**](#datetime)
    - alternate types:
      - [**date**](#date)
  - if this property is used on a `Journal` component it represents the instant referred
  - e.g. 2020-03-19T14:00:00Z

## `created`

Date of creation of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.7.1)
  - type: [**date-time**](#datetime)
  - e.g. 2020-03-19T14:00:00Z

## `categories`

Categories of the component

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.1.2)
  - type: [**text**](#text)
  - supports multiple values by using an array
  - e.g. "Math", "Lecture", ["Evaluation", "Assignment", "Cyber Security"]

## `relatedTo`

Unique identifiers of other components the component is related to

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.4.5)
  - type: [**text**](#text)

## `due`

Defines the date and time that the component is expected to be completed

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.2.3)
  - type: [**date-time**](#datetime)
    - alternate types:
      - [**date**](#date)
  - e.g. 2020-03-19T00:00:00Z


## `dtend`

Ending date

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.2.2)
  - the `endDate` should be after `startDate`
  - type: [**date-time**](#datetime)
    - alternate types:
      - [**date**](#date)
        - if this data type is used for `dtend` then it **must** be used for `dtstart` aswell
  - e.g. 2020-03-19T16:30:00Z

## `rrule`
Recurrency rule of the component. Only used in `Event`s

[RFC](https://tools.ietf.org/html/rfc5545#section-3.8.5.3)
  - type: [**recur**](https://tools.ietf.org/html/rfc5545#section-3.3.10)

# Data types

## Text
A sequence of characters. What characters are allowed or not is defined in the iCalendar RFC.

[RFC](https://tools.ietf.org/html/rfc5545#section-3.3.11)

## Date
A date with the format: YYYYmmDD
- Y - Year
- m - Month
- D - Day of Month

[RFC](https://tools.ietf.org/html/rfc5545#section-3.3.4)

## Datetime
A date with the format: YYYYmmDDThhMMssZ
- Y - Year
- m - Month
- D - Day of Month
- h - Hour
- M - Minute
- s - Second
- Z - The Z will appear if the time is in UTC time

[RFC](https://tools.ietf.org/html/rfc5545#section-3.3.5)

## URI
A Unique Resource Identifier as described [here](https://tools.ietf.org/html/rfc3986#section-3).

[RFC](https://tools.ietf.org/html/rfc5545#section-3.3.13)