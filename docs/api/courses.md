# `Course`
A `Course`, also known as `Curricular Unit`, is an academic endeavor composed by classes, lecturers, etc. The full representation of a `Course`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
* `acronym`: the course's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "DWA"

* `name`: the course's name
  - type: **text**
  - e.g. "Development of Web Applications"

## Link relations
A course representation:
* *must* include a link to its context, using the `self` link relation
* *may* include links to its classes, using the `/rel/class` link relation
* *may* include links to its events, using the `/rel/event` link relation

## Actions
A course representation includes a description of the available actions the client may want to apply. The procedure of each action (payload, media types, method, location) is described in the same section.

The available actions are:
* `follow`: subscribe to the course's events
  - unsafe
  - not templated

## Example representation
```json
{
  "class": [ "course" ],
  "properties": { 
      "acronym": "DWA"
  },
  "entities": [
    {
      "class": [ "class", "collection" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/courses/dwa/classes" },
        { "rel": [ "course" ], "href": "/courses/dwa" }
      ]
    },
    {
      "class": [ "event", "collection" ],
      "rel": [ "/rel/event" ], 
      "links": [
        { "rel": [ "self" ], "href": "/courses/dwa/events" },
        { "rel": [ "course" ], "href": "/courses/dwa" }  
      ]
    }
  ],
  "actions": [
    {
      "name": "follow",
      "title": "Follow",
      "method": "POST",
      "href": "/courses/dwa/follow",
      "type": "application/x-www-form-urlencoded",
      "fields": [ ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/courses/dwa" }
  ]
}
```

# `Course Item`

A simplified representation of a `course`. This is how `course`s are represented as individual `collection` items.

## Properties
* `acronym`: the course's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "DWA"

## Link relations
A course item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `course collection` it belongs to, using the `collection` link relation

# `Course Collection`

## Properties
* `size`: the total number of `course`s available.
  - type: **integer**
  - e.g. 22

## Query parameters
This resource is templated, meaning tokens can add detail to the request. All of these are optional.

The following tokens are available:
* `limit`: the preferred maximum number of items, between 1 and 100, 10 included in the response
  - type: **integer**
  - default: 15

* `page`: multiplies with `limit` to specify what block of the whole collection to return
  - type: **integer**
  - default: 0

## Link relations
A course collection representation:
* *must* include a link to itself, using the `self` link relation
* *may* include links to a number of its classes, using the `item` link relation

## Actions
A course collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `add`: adding a new course to the collection.
  - unsafe
  - not templated

## Example representation
```json
{
  "class": [ "course", "collection" ],
  "properties": { 
      "size": 20
  },
  "entities": [
    {
      "class": [ "course" ],
      "rel": [ "item" ], 
      "properties": { 
        "acronym": "DWA"
      },
      "links": [
        { "rel": [ "self" ], "href": "/courses/dwa" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "item" ], 
      "properties": { 
        "acronym": "SL"
      },
      "links": [
        { "rel": [ "self" ], "href": "/courses/sl" }
      ]
    }
  ],
  "actions": [
  ],
  "links": [
    { "rel": [ "self" ], "href": "/courses?page=1&limit=2" },
    { "rel": [ "next" ], "href": "/courses?page=2&limit=2" },
    { "rel": [ "previous" ], "href": "/courses?page=0&limit=2" }
  ]
}
```


