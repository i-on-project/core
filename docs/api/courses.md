# `Course`
A `Course`, also known as `Curricular Unit`, is an academic endeavor composed by classes, lecturers, etc. The full representation of a `Course`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
* `acronym`: the course's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "WAD"

* `name`: the course's name
  - type: **text**
  - e.g. "Web Application Development"

## Link relations
A course representation:
* *must* include a link to its context, using the `self` link relation
* *may* include links to its classes, using the `/rel/class` link relation
* *may* include links to its events, using the `/rel/event` link relation

## Example representation
```json
{
  "class": [ "course" ],
  "properties": { 
      "acronym": "WAD",
	  "name": "Web Application Development"
  },
  "entities": [
    {
      "class": [ "class", "collection" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/courses/wad/classes" },
        { "rel": [ "course" ], "href": "/courses/wad" }
      ]
    },
    {
      "class": [ "event", "collection" ],
      "rel": [ "/rel/event" ], 
      "links": [
        { "rel": [ "self" ], "href": "/courses/wad/events" },
        { "rel": [ "course" ], "href": "/courses/wad" }  
      ]
    }
  ],
  "actions": [
  ],
  "links": [
    { "rel": [ "self" ], "href": "/courses/wad" }
  ]
}
```

# `Course Item`

A simplified representation of a `course`. This is how `course`s are represented as individual `collection` items.

## Properties
* `acronym`: the course's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "WAD"

## Link relations
A course item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `course collection` it belongs to, using the `collection` link relation

# `Course Collection`

## Link relations
A course collection representation:
* *must* include a link to itself, using the `self` link relation
* *may* include links to a number of its classes, using the `item` link relation

## Actions
A course collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `add-item`: adding a new course to the collection.
  - unsafe
  - not templated

* `search`:  search for the collection's items.
  - safe
  - templated

## Fields
The following fields are parameters of the action `search`:
* `limit`: the preferred maximum number of items, between 1 and 100, 10 included in the response
  - type: **number**
  - default: 15

* `page`: multiplies with `limit` to specify what block of the whole collection to return
  - type: **number**
  - default: 0

## Example representation
```json
{
  "class": [ "course", "collection" ],
  "properties": { },
  "entities": [
    {
      "class": [ "course" ],
      "rel": [ "item" ], 
      "properties": { 
        "acronym": "WAD"
      },
      "links": [
        { "rel": [ "self" ], "href": "/courses/wad" },
        { "rel": [ "collection" ], "href": "/courses" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "item" ], 
      "properties": { 
        "acronym": "SL"
      },
      "links": [
        { "rel": [ "self" ], "href": "/courses/sl" },
        { "rel": [ "collection" ], "href": "/courses" }
      ]
    }
  ],
  "actions": [
    {
      "name": "add-item",
      "title": "Add a new Course",
      "method": "POST",
      "href": "/v0/courses",
      "isTemplated": false,
      "type": "application/json",
      "fields": [ ]
    },
    {
      "name": "search",
      "title": "Search items",
      "method": "GET",
      "href": "/v0/courses{?limit,page}",
      "isTemplated": true,
      "type": "application/x-www-form-urlencoded",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/courses?page=1&limit=2" },
    { "rel": [ "next" ], "href": "/courses?page=2&limit=2" },
    { "rel": [ "previous" ], "href": "/courses?page=0&limit=2" }
  ]
}
```


