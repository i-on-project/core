# `Class Section`
A subdivision of a `Class`, in the context of a `Course`. The full representation of a `Class Section`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
* `course`: the course's unique acronym
  - type: **text**
  - e.g. "DWA"

* `class`: the class section parent class's identifier (term)
  - type: **text**
  - e.g. "s1920i"

* `id`: unique identifier, which distinguishes class sections
  - type: **text**
  - e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to its context, using the `self` link relation
* *may* include links to its events, using the `/rel/event` link relation

## Actions
A `class section` representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

* `enroll`: subscribe to the class section's events
  - unsafe
  - not templated

# `Class Section Item`
A `Class Section` as an item to a `Class`. This is the shortened representation of a `Class Section`.

## Properties

* `id`: unique identifier, which distinguishes class sections
  - type: **text**
  - e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to its context, using the `self` link relation
* *may* include a link to the `class` it belongs to, using the `collection` link relation

## Example representation
```json
{
  "class": [ "class", "section" ],
  "properties": { 
    "course": "DWA",
    "class": "s1920v",
    "id": "1D"
  },
  "entities": [
    {
      "class": [ "event", "collection" ],
      "rel": [ "/rel/event" ], 
      "links": [
        { "rel": [ "self" ], "href": "/v0/courses/dwa/classes/s1920v/1d/events" }
      ]
    }
  ],
  "actions": [
    {
      "name": "enroll",
      "title": "Enroll class section",
      "method": "POST",
      "href": "/v0/courses/dwa/classes/s1920v/1d/enroll",
      "type": "application/x-www-form-urlencoded",
      "fields": [ ]
    }
  ],
  "links": [
    { "rel": [ "self" ], "href": "/v0/courses/dwa/classes/s1920v/1d" },
    { "rel": [ "collection" ], "href": "/v0/courses/dwa/classes/s1920v" }
  ]
}
```
