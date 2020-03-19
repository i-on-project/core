# `Class Sections`
A subdivision of a `Class`, in the context of a `Course`. The full representation of a `Class`, in the [application/vnd.siren+json](https://github.com/kevinswiber/siren) media type.

## Properties
* `course`: the course's unique acronym
	- type: **string**
	- e.g. "DWA"

* `class`: the class section parent class's identifier (term)
	- type: **string**
	- e.g. "s1920i"

* `id`: unique identifier, which distinguishes class sections
	- type: **string**
	- e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to itself, using the `self` link relation
* *may* include links to its events, using the `/rel/event` link relation

## Actions
A `class section` representation includes a description of the available actions the client may want to apply. Details on how the client should go around applying such actions are described in the message itself.

* `follow` - subscribe to the class section's events
	- unsafe
	- not templated

# `Class Section Items`
A `Class Section` as an item to a `Class`. This is the shortened representation of a `Class Section`.

## Properties

* `id`: unique identifier, which distinguishes class sections
	- type: **string**
	- e.g. "1D"

## Link relations
A class section representation:
* *must* include a link to itself, using the `self` link relation
* *may* include a link to the `class` it belongs to, using the `collection` link relation

## Example
```javascript
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
      "name": "follow",
      "title": "Follow",
      "method": "POST",
      "href": "/v0/courses/dwa/classes/s1920v/1d/follow",
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
