- [Search](#search)
  - [Object](#object)
    - [Sub-entities](#sub-entities)
      - [Class](#class)
      - [Properties](#properties)
        - [Schema](#schema)
      - [Links](#links)
    - [Links](#links-1)
    - [Example Representation](#example-representation)
  - [Querying](#querying)
    - [Query](#query)
    - [Types](#types)
    - [Limit](#limit)
    - [Page](#page)
      - [Invalid Page](#invalid-page)
  - [Errors](#errors)
    - [Invalid Query](#invalid-query)
    - [Invalid Type](#invalid-type)

# Search

## Object
### Sub-entities
#### Class
Will dictate what kind of resource the result represents. The possible `class`es match the possible `types` to be used in the query string. They can be found [here](#types).
For example, a sub-entity with `"class" : [ "course" ]` is a `Course` search result.

#### Properties
##### Schema
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

#### Links
The sub-entity will have a link to the resource with the `self` relation.

### Links
The collection provides some `links`
* `self` - link to the current page of the collection
* `previous` - link to the previous page. Will not appear if self links to the first page of the collection
* `next` - link to the next page. Will not appear if self links to the last page of the collection

### Example Representation
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
## Querying

Querying is done with a request to the path specified in the [Home resource](root.md) with the possible query paramters in the query string:
* [query](#query)
* [types](#types)
* [limit](#limit)
* [page](#page)

### Query

The query parameter is treated as a `string`. Any words used in the search do not need to be complete. For example, `lab` would match with anything with the word `Laboratory`. If multiple words are used, they should be separated by **spaces** and the results may not have all of them.

### Types

The available types consist of:
* `programme` - [Link](./programme.md)
* `calendar-term` - [Link](./calendar-terms.md)
* `course` - [Link](./courses.md)
* `class` - [Link](./classes.md)
* `class-section` - [Link](./class-sections.md)

If no types are specified, all are used. 

When specifying multiple types, separate them with commas, such as: `types=programme,course,class-section`.

### Limit

Integer value used to limit the number of search results returned. Default is 10.

### Page

Integer value used to specify the page of the search results to return. Default is 1.

#### Invalid Page

If the page has no results and the number used is 1, the response will have a 200 status code and a `search` object with an empty `entities` array as a body.

If the page has no results and the number used is more than 1, the response will have a 404 status code instead.

## Errors

### Invalid Query

The query used must only contain alpha-numeric(letters and numbers) or space characters.

In the future other characters may be allowed to attribute a different meaning to the search, such as, adding `!` before a word will make it so the results won't contain the word.

### Invalid Type

One or multiple types were used that are not specified in the [Types](#types) section.

Also make sure there are no spaces in between types. `types=course,programme` and **not** `types=course, programme`.