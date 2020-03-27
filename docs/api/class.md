# `Class`

The `Class` endpoint returns all classes of all calendar terms of given `Course`, the following example returns all classes of course "WAD". The full representation of a `Class`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

Parameter size of properties indicates the total number of calendar semesters that the course WAD was taught. 

## Properties
* `course`: the course's unique acronym
  - type: **text**
  - e.g. "WAD"

* `calendar term`:
  - type: **text**
  - e.g. "WAD"

## Link relations
A class representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the collection it belongs, using the `collection` link relation
* *may* include links to its events, using the `/rel/event` link relation

## Actions
* `delete`: delete the class
  - unsafe
  - not templated

* `edit`: edit or add a new class
  - unsafe
  - not templated
  
## Example representation
Given the different Classes a client may want to consume the details of a class at a certain calendar semester, the response indicates all the details like the coordinator, hours, designation...
The response links follows the graph path, from this point you can check the events of that class or go back and choose a different class.

```json
{
  "class" : [ "class" ],
  "properties": {
    "course": "WAD",
    "calendar term": "1920v"
  },
  "entities": [
    {
      "class": [ "classSection", "collection" ],
      "rel": [ "/rel/section" ],
      "title": "ClassSections of Course WAD at Calendar Term 1920v",
      "href": "/v0/courses/wad/classes/1920v/classSection"
    }
  ],
  "actions": [
    {
      "name": "delete",
      "title": "Delete class",
      "method": "DELETE",
      "isTemplated": false,
      "href": "/v0/courses/wad/classes/1920v",
      "fields": [ ]
    },
    {
      "name": "edit",
      "title": "Edit class",
      "method": "PATCH",
      "isTemplated": false,
      "type": "application/json",
      "href": "/v0/courses/wad/classes/1920v",
      "fields": [ ]
    } 
  ],
  "links": [
    { "rel" : [ "self" ], "href": "/v0/courses/wad/classes/1920v" },
    { "rel" : [ "collection" ], "href": "/v0/courses/wad/classes/" },
    { "rel" : [ "events" ], "href": "/v0/courses/wad/classes/1920v/events" }
  ]
}
```

# `Class Collection`

A collection of `class section`s which serve the purpose of organizing the `class`'s `events`.
For instance, each `lecturer` can be in charge of `events` like lectures or assignments for a number of `students`.

## Properties
* `size`: the total number of `course`s available.
  - type: **number**
  - e.g. 22

## Link relations
A class representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the course that contains these classes, using the `about` link relation

## Actions
* `add-item`: add a new class
  - unsafe
  - not templated

* `batch-delete`: delete multiple items of the collection using the query string
  - unsafe
  - templated
  
## Fields
The following fields are parameters of the action `search`:
* `limit`: the preferred maximum number of items, between 1 and 100, 10 included in the response
  - type: **number**
  - default: 15

* `page`: multiplies with `limit` to specify what block of the whole collection to return
  - type: **number**
  - default: 0

The following fields are parameters of the action `batch-delete`:
* `course`: delete all classes of the specified course
  - type: **text**

* `term`: delete all classes of the specified term
  - type: **text**
  
## Example representation
```json
{
  "class" : [ "collection", "class" ],
  "properties": {
    "course": "WAD",
    "size": 5
  },
  "entities" : [
    {
      "class": [ "class" ],
      "rel": [ "item" ],
      "title": "WAD Class during the 1920v semester",
      "href": "/v0/courses/wad/classes/1920v"
    },
    {
      "class": [ "class" ],
      "rel": [ "item" ],
      "title": "WAD Class during the 1920i semester",
      "href": "/v0/courses/wad/classes/1920i"
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search items",
      "method": "GET",
      "href": "/v0/courses/wad/classes{?limit,page}",
      "isTemplated": true,
      "type": "application/vnd.siren+json",
      "fields": [
        { "name": "limit", "type": "number", "class": "param/limit" },
        { "name": "page", "type": "number", "class": "param/page" }
      ]
    },
    {
      "name": "add-item",
      "title": "Add Item",
      "method": "POST",
      "href": "/v0/courses/wad/classes",
      "isTemplated": false,
      "type": "application/json",
      "fields": [ ]
    },
    {
      "name": "batch-delete",
      "title": "Delete multiple items",
      "method": "DELETE",
      "isTemplated": true,
      "href": "/v0/courses/wad/classes{?term,course}",
      "fields": [
        { "name": "term", "type": "text" },
        { "name": "course", "type": "text" }
      ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes" },
    { "rel": [ "about" ], "href": "/v0/courses/wad" }
  ]
}
```
