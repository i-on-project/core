- [Search](#search)
  - [Sub-entities](#sub-entities)
    - [Class](#class)
    - [Properties](#properties)
      - [Schema](#schema)
    - [Links](#links)
  - [Links](#links-1)
  - [Example Representation](#example-representation)

# Search

## Sub-entities
### Class
Will dictate what kind of resource the result represents.
For example, a sub-entity with `"class" : [ "course" ]` is a `Course` search result.

### Properties
#### Schema
All the sub-entities will have the same `properties` schema, which will be:
```json
{
    "$schema": "http://json-schema.org/draft-07/schema",
    "$id": "https://github.com/i-on-project/core/blob/master/docs/api/search.md#schema",
    "type": "object",
    "title": "Search Result properties schema",
    "description": "This is the schema for Search Result properties.",
    "properties": {
        "id": {
            "$id": "#/properties/id",
            "type": "string",
            "title": "Id of the entity",
            "description": "The id of the entity represented in the search result.",
            "examples": [
                "12",
                "1D",
                "1819v"
            ]
        },
        "name": {
            "$id": "#/properties/name",
            "type": "string",
            "title": "Name associated with the search result",
            "description": "A name is associated with every search result so a client has something to display.",
            "examples": [
                "Web App Development"
            ]
        }
    },
    "required": [
        "id",
        "name"
    ]
}
```

### Links
The sub-entity will have a link to the resource with the `self` relation.

## Links
The collection provides some `links`
* `self` - link to the current page of the collection
* `previous` - link to the previous page. Will not appear if self links to the first page of the collection
* `next` - link to the next page. Will not appear if self links to the last page of the collection

## Example Representation
```json
{
    "class": [ "search", "result", "collection" ],
    "entities": [
        {
            "class": [ "course" ],
            "properties": {
                "id": "1",
                "name": "Software Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "self" ],
                    "href": "/v0/courses/1"
                }
            ]
        },
        {
            "class": [ "course" ],
            "properties": {
                "id": "4",
                "name": "Hardware Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "self" ],
                    "href": "/v0/courses/4"
                }
            ]
        },
        {
            "class": [ "class" ],
            "properties": {
                "id": "56",
                "name": "HL 1920i"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "self" ],
                    "href": "/v0/courses/4/classes/1920i"
                }
            ]
        },{
            "class": [ "course" ],
            "properties": {
                "id": "5",
                "name": "Computer IT Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "self" ],
                    "href": "/v0/courses/5"
                }
            ]
        },
        {
            "class": [ "todo" ],
            "properties": {
                "id": "545",
                "name": "Hardware Laboratory - First assignment"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "self" ],
                    "href": "/v0/courses/4/21D/calendar/545"
                }
            ]
        }
    ],
    "links": [
        {
            "rel": [ "self" ],
            "href": "/v0/search?query=Lab&limit=5"
        },
        {
            "rel": [ "next" ],
            "href": "/v0/search?query=Lab&limit=5&page=2"
        }
    ]
}
```