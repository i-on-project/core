# `Term`
A `Term` is a period during which an educational institution holds academic `Events`. The full representation of a `Term`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
* `name`: the term's name
  - type: **text**
  - e.g. "1819v"

## Link relations
A term representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the collection it belongs, using the `collection` link relation
* *may* include links to a number of classes that were available during said term, using the `/rel/class` link relation

## Actions
A term collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

The available actions are:
* `search`: search for classes available during this term.
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
  "class": [ "term" ],
  "properties": {
      "name": "1920v"
  },
  "entities": [
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/wad/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses/wad/classes" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/sl/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses/sl/classes" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search classes in a term",
      "method": "GET",
      "href": "/v0/terms/1920v{?limit,page}",
      "isTemplated": true,
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/terms/1920v?limit=2" },
    { "rel": [ "next" ], "href": "/v0/terms?page=1&limit=2" },
    { "rel": [ "collection" ], "href": "/v0/terms" }
  ]
}
```

# `Term Item`

A simplified representation of a `term`. This is how `term`s are represented as individual `collection` items.

## Properties
* `name`: the term's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "1920v"

## Link relations
A term item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `term collection` it belongs to, using the `collection` link relation

# `Term Collection`

## Link relations
A term collection representation:
* *must* include a link to itself, using the `self` link relation
* *may* include links to a number of its terms, using the `item` link relation

## Actions
A term collection representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

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
  "class": [ "term", "collection" ],
  "properties": { },
  "entities": [
    {
      "class": [ "term" ],
      "rel": [ "item" ], 
      "properties": { 
        "name": "1920v"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/terms/1920v" },
        { "rel": [ "collection" ], "href": "/v0/terms" }
      ]
    },
    {
      "class": [ "term" ],
      "rel": [ "item" ], 
      "properties": { 
        "name": "1920i"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/terms/1920i" },
        { "rel": [ "collection" ], "href": "/v0/terms" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search items",
      "method": "GET",
      "href": "/v0/terms{?limit,page}",
      "isTemplated": true,
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/terms?page=1&limit=2" },
    { "rel": [ "next" ], "href": "/v0/terms?page=2&limit=2" },
    { "rel": [ "previous" ], "href": "/v0/terms?page=0&limit=2" }
  ]
}
```


