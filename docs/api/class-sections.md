# `Class Section`
A `class section` is a subdivision of a `class`, with a dedicated calendar of `event`s.

The `class section` resource has two possible representations: a detailed (or full) representation as well as a reduced one (to be used as `item`s of classes). The following examples demonstrate the different representations of a `class section` for a given `class` "WAD, s1920v". The different representations of a `class section` are in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
All properties which are not assigned with the `mandatory` label, are optional, they may not be included in the representation.

* `courseId`: the course's unique identifier
  - mandatory
  - type: **number**
  - e.g. 1

* `courseAcr`: the course's unique acronym
  - type: **text**
  - e.g. "WAD"

* `class`: the class section parent class's identifier (term)
  - mandatory
  - type: **text**
  - e.g. "s1920v"

* `id`: unique identifier, which distinguishes class sections
  - mandatory
  - type: **text**
  - e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to its calendar, using the `/rel/calendar` link relation
* *may* include a link to the class it belongs to, using the `collection` link relation

## Actions
A `class section` representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

* `enroll`: subscribe to the class section's events
  - unsafe
  - not templated

## Example representation
```json
{
  "class": [ "class", "section" ],
  "properties": { 
    "courseAcr": "WAD",
    "courseId": 1,
    "class": "s1920v",
    "id": "1D"
  },
  "entities": [
    {
      "class": [ "calendar" ],
      "rel": [ "/rel/calendar" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/1/classes/s1920v/1d/calendar" }
      ]
    }
  ],
  "actions": [
    {
      "name": "enroll",
      "title": "Enroll class section",
      "method": "POST",
      "href": "/v0/courses/1/classes/s1920v/1d/enroll",
      "type": "application/x-www-form-urlencoded",
      "fields": [ ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/1/classes/s1920v/1d" },
    { "rel": [ "collection" ], "href": "/v0/courses/1/classes/s1920v" }
  ]
}
```

# `Class Section Item`
A `class section` as an item to a `class`. This is the shortened representation of a `class section`.

## Properties

* `id`: unique identifier, which distinguishes class sections
  - type: **text**
  - e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `class` it belongs to, using the `collection` link relation
