# `Class`

The `Class` endpoint returns all classes of all calendar terms of given `Course`, the following example returns all classes of course "WAD". The full representation of a `Class`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

Parameter size of properties indicates the total number of calendar semesters that the course WAD was taught. 

## Properties
* `course`: the course's unique acronym
  - type: **text**
  - e.g. "WAD"

* `coordinator`: the `lecturer` responsible for managing the `class`
  - type: **text**
  - e.g. "WAD"

* `calendar term`:
  - type: **text**
  - e.g. "WAD"

* `mandatory`:
  - type: **bool**
  - e.g. true

* `credits`:
  - type: **number**
  - e.g. 6

* `hours of work`:
  - type: **number**
  - e.g. 123

* `programme`:
  - type: **text**
  - e.g. "CSCE"

## Example representation
Given the different Classes a client may want to consume the details of a class at a certain calendar semester, the response indicates all the details like the coordinator, hours, designation...
The response links follows the graph path, from this point you can check the events of that class or go back and choose a different class.

```json
{
  "class" : [ "classDetails" ],
  "properties": {
    "course": "WAD",
    "coordinator": "Pedro Félix",
    "calendar term": "1920v",
    "mandatory": "No",
    "credits": "6",
    "year": "3",
    "curricularTerm": "6",
    "hours of work": "162",
    "programme": "CSCE"
  },
  "entities": [
    {
      "class": [ "classSection", "collection" ],
      "rel": [ "/rels/classDetails" ],
      "title": "ClassSections of Course WAD at Calendar Term 1920v",
      "href": "/v0/courses/wad/classes/1920v/classSection"
    }
  ],
  "actions": [],
  "links": [
    { "rel" : [ "self" ], "href": "/v0/courses/wad/classes/1920v" },
    { "rel" : [ "class" ], "href": "/v0/courses/wad/classes/" },
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
    },
    ...
  ],
  "actions": [],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/wad/classes" },
    { "rel": [ "courses" ], "href": "/v0/courses/" }
  ]
}
```
