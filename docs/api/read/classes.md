# `Class`

A `class` is an instance of a `course` in a given `calendar term`, with a calendar of `event`s associated.

The `class` resource has three possible representations: a detailed (or full) representation, a reduced one (to be used as `item`s of class collections) and a `collection` representation. The following examples demonstrate the different representations of a `class` for a given `course` "WAD". The different representations of a `class` are in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `courseAcr`: the course's unique acronym
  - type: **text**
  - e.g. "WAD"

* `courseId`: the course's unique identifier
  - mandatory
  - type: **number**
  - e.g. 1

* `calendar term`: period when the academic `event`s of the `course` will occur
  - mandatory
  - type: **text**
  - e.g. "1920v"

## Link relations
A class representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the collection it belongs, using the `collection` link relation
* *may* include links to its `class section`s, using the `item` link relation
* *may* include a link to its calendar of events, using the `/rel/calendar` link relation

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

Since the number of class sections is thought to be very reduced, all of them will be included in the payload as `item`s.

```json
{
  "class" : [ "class" ],
  "properties": {
    "courseAcr": "WAD",
    "courseId": 1,
    "calendarTerm": "1920v"
  },
  "entities": [
    {
      "class": [ "class", "section" ],
      "properties": {
        "courseId": 1,
        "courseAcr": "SL",
        "calendarTerm": "1920i",
        "id": "1D"
      },
      "rel": [ "item" ],
      "links": [
      { "rel" : [ "self" ], "href": "/v0/courses/1/classes/1920v/1D" },
      ]
    },
    {
      "class": [ "class", "section" ],
      "properties": {
        "courseId": 1,
        "courseAcr": "SL",
        "calendarTerm": "1920i",
        "id": "2D"
      },
      "rel": [ "item" ],
      "links": [
        { "rel" : [ "self" ], "href": "/v0/courses/1/classes/1920v/2D" },
      ]
    },
    {
      "class": [ "calendar" ],
      "rel": [ "/rel/calendar" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes/s1920v/calendar" }
      ]
    }
  ],
  "actions": [
    {
      "name": "delete",
      "method": "DELETE",
      "isTemplated": false,
      "href": "/v0/courses/1/classes/1920v",
      "fields": [ ]
    },
    {
      "name": "edit",
      "title": "Edit class",
      "method": "PATCH",
      "isTemplated": false,
      "type": "*/*",
      "href": "/v0/courses/1/classes/1920v",
      "fields": [ ]
    } 
  ],
  "links": [
    { "rel" : [ "self" ], "href": "/v0/courses/1/classes/1920v" },
    { "rel" : [ "collection" ], "href": "/v0/courses/1/classes/" }
  ]
}
```

# `Class Item`

A simplified representation of a `class`. This is how `class`es are represented as individual `collection` items.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `calendarTerm`: the calendar term which the class takes place
  - mandatory
  - type: **text**
  - e.g. 1718v

## Link relations
A class item representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `course collection` it belongs to, using the `collection` link relation

# `Class Collection`

Lists out the `course`'s classes for different semesters.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `courseAcr`: the course's unique acronym
  - type: **text**
  - e.g. "WAD"

* `courseId`: the course's unique identifier
  - mandatory
  - type: **number**
  - e.g. 1

## Link relations
A class representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the currently active class (current calendar term), using the `current` link relation
* *may* include a link to the course that contains these classes, using the `about` link relation

## Actions
* `search`: find groups of classes matching the specified parameters
  - safe
  - not templated

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
    "courseId": 1
  },
  "entities" : [
    {
      "class": [ "class" ],
      "rel": [ "item" ],
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920v" }
      ]
    },
    {
      "class": [ "class" ],
      "rel": [ "item" ],
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes/1920i" }
      ]
    }
  ],
  "actions": [
    {
      "name": "search",
      "title": "Search items",
      "method": "GET",
      "href": "/v0/courses/1/classes{?limit,page}",
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
      "href": "/v0/courses/1/classes",
      "isTemplated": false,
      "type": "application/json",
      "fields": [ ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes" },
    { "rel": [ "about" ], "href": "/v0/courses/1" }
  ]
}
```
