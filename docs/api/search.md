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
All search results will be sub-entities with both `"search"` and `"result"` strings in their `class`. The remaining strings will dictate what kind of resource the result represents.
For example, a sub-entity with `"class" : [ "course", "search", "result" ]` is a `Course` search result.

### Properties
#### Schema
All the sub-entities will have the same `properties` schema, which will be:
```json
{
    "$schema": "http://json-schema.org/draft-07/schema",
    "$id": "http://github.com/i-on-project/core/docs/api/search.md#schema",
    "type": "object",
    "title": "Search Result properties schema",
    "description": "This is the schema for Search Result properties.",
    "properties": {
        "id": {
            "$id": "#/properties/id",
            "type": "integer",
            "title": "Id of the entity",
            "description": "The id of the entity represented in the search result.",
            "default": 0,
            "examples": [
                12
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
Since the sub-entity is more of a link to a resource than a representation of one a `link`  with the `self` relation doesn't exist. Consequently, a `link` object with `rel` equal to the `class` minus `"search"` & `"result"`, will provide a `href` to the resource.
Example: `"class": [ "class", "section", "search", "result" ]` means that to fetch this resource the link with `"rel": [ "class", "section" ]` is the one whose `href` is needed.

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
            "class": [ "course", "search", "result" ],
            "properties": {
                "id": 1,
                "name": "Software Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "course" ],
                    "href": "/v0/courses/1"
                }
            ]
        },
        {
            "class": [ "course", "search", "result" ],
            "properties": {
                "id": 4,
                "name": "Hardware Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "course" ],
                    "href": "/v0/courses/4"
                }
            ]
        },
        {
            "class": [ "class", "search", "result" ],
            "properties": {
                "id": 56,
                "name": "Hardware Laboratory 1920i"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "class" ],
                    "href": "/v0/courses/4/classes/1920i"
                }
            ]
        },{
            "class": [ "course", "search", "result" ],
            "properties": {
                "id": 5,
                "name": "Computer IT Laboratory"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "course" ],
                    "href": "/v0/courses/5"
                }
            ]
        },
        {
            "class": [ "todo", "search", "result" ],
            "properties": {
                "id": 545,
                "name": "Hardware Laboratory - First assignment"
            },
            "rel": [ "item" ],
            "links": [
                {
                    "rel": [ "todo" ],
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