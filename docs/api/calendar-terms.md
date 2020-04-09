# `Calendar Term`
A `calendar term` is a period during which an educational institution holds academic `events`.

The `calendar term` resource has three possible representations: a detailed (or full) representation, a reduced one (to be used as `item`s of calendar term collections) and a `collection` representation. The different representations of a `calendar term` are in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `id`: the calendar term's unique identifier
  - mandatory
  - type: **text**
  - e.g. "1819v"

## Link relations
A calendar term representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the collection it belongs, using the `collection` link relation
* *may* include links to a number of classes that were available during said calendar term, using the `/rel/class` link relation

## Actions
A calendar term collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for classes available during this calendar term.
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
  "class": [ "calendar-term" ],
  "properties": {
      "name": "1920v"
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses/1/classes" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/2/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses/2/classes" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search classes in a calendar term",
      "method": "GET",
      "href": "/v0/calendar-terms/1920v{?limit,page}",
      "isTemplated": true,
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/calendar-terms/1920v?limit=2" },
    { "rel": [ "next" ], "href": "/v0/calendar-terms?page=1&limit=2" },
    { "rel": [ "collection" ], "href": "/v0/calendar-terms" }
  ]
}
```

# `Calendar Term Item`

A simplified representation of a `calendar term`. This is how `calendar term`s are represented as individual `collection` items.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `id`: the calendar term's unique identifier
  - mandatory
  - type: **text**
  - e.g. "1920v"

## Link relations
A calendar term item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `calendar term collection` it belongs to, using the `collection` link relation

# `Calendar Term Collection`

## Link relations
A calendar term collection representation:
* *must* include a link to itself, using the `self` link relation
* *may* include links to a number of its calendar term, using the `item` link relation

## Actions
A calendar term collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for the collection's items.
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
  "class": [ "calendar-term", "collection" ],
  "properties": { },
  "entities": [
    {
      "class": [ "term" ],
      "rel": [ "item" ], 
      "properties": { 
        "name": "1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/calendar-terms/1920v" },
        { "rel": [ "collection" ], "href": "/v0/calendar-terms" }
      ]
    },
    {
      "class": [ "term" ],
      "rel": [ "item" ], 
      "properties": { 
        "name": "1920i"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/calendar-terms/1920i" },
        { "rel": [ "collection" ], "href": "/v0/calendar-terms" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search items",
      "method": "GET",
      "href": "/v0/calendar-terms{?limit,page}",
      "isTemplated": true,
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/calendar-terms?page=1&limit=2" },
    { "rel": [ "next" ], "href": "/v0/calendar-terms?page=2&limit=2" },
    { "rel": [ "previous" ], "href": "/v0/calendar-terms?page=0&limit=2" }
  ]
}
```


