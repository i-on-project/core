# `Course`
A `course`, also known as `curricular unit`, is an academic endeavor composed by `classes`, academic `events`, etc.

The `course` resource has three possible representations: a detailed (or full) representation, a reduced one (to be used as `item`s of course collections) and a `collection` representation. The following examples demonstrate the different representations of `course`s. The different representations of a `course` are in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `id`: the course's unique identifier
  - mandatory
  - type: **number**
  - e.g. 1

* `acronym`: the course's unique acronym; an abbreviation of its name
  - type: **text**
  - e.g. "WAD"

* `name`: the course's name
  - type: **text**
  - e.g. "Web Application Development"

## Link relations
A course representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the collection it belongs, using the `collection` link relation
* *may* include a link to the currently active class (current semester), using the `/rel/current` link relation
* *may* include links to its classes, using the `/rel/class` link relation

## Actions
* `delete`: delete the course
  - unsafe
  - not templated

* `edit`: edit or add a new course
  - unsafe
  - not templated

## Example representation
```json
{
  "class": [ "course" ],
  "properties": { 
    "id": 1,
    "acronym": "WAD",
	"name": "Web Application Development"
  },
  "entities": [
    {
      "class": [ "class", "collection" ],
      "rel": [ "/rel/class" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes" },
        { "rel": [ "about" ], "href": "/v0/courses/1" }
      ]
    }
  ],
  "actions": [
    {
      "name": "delete",
      "title": "Delete course",
      "method": "DELETE",
      "isTemplated": false,
      "href": "/v0/courses/1",
      "fields": [ ]
    },
    {
      "name": "edit",
      "title": "Edit course",
      "method": "PATCH",
      "isTemplated": false,
      "type": "application/json",
      "href": "/v0/courses/1",
      "fields": [ ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1" },
    { "rel": [ "current" ], "href": "/v0/courses/1/classes/1920v" },
    { "rel": [ "collection" ], "href": "/v0/courses" }
  ]
}
```

# `Course Item`

A simplified representation of a `course`. This is how `course`s are represented as individual `collection` items.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `id`: the course's unique identifier
  - mandatory
  - type: **number**
  - e.g. 1

* `acronym`: the course's unique acronym; an abbreviation of its name
  - mandatory
  - type: **text**
  - e.g. "WAD"

## Link relations
A course item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `course collection` it belongs to, using the `collection` link relation
* *may* include a link to the currently active class (current semester), using the `/rel/current` link relation

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

* `search`: find groups of courses matching the specified parameters
  - safe
  - not templated

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
	    "id": 1,
        "acronym": "WAD"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1" },
		{ "rel": [ "current" ], "href": "/v0/courses/1/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "item" ], 
      "properties": { 
	    "id": 2,
        "acronym": "SL"
      },
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/2" },
		{ "rel": [ "current" ], "href": "/v0/courses/2/classes/1920v" },
        { "rel": [ "collection" ], "href": "/v0/courses" }
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
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses?page=1&limit=2" },
    { "rel": [ "next" ], "href": "/v0/courses?page=2&limit=2" },
    { "rel": [ "previous" ], "href": "/v0/courses?page=0&limit=2" }
  ]
}
```


