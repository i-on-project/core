# Programme Collection
A collection of the possible programmes.

## Link Relations
   * A programme representation
    - must include a link to its context, using the self link relation.
    - must include links to the details of a programme, using the `/rel/programme` relation.

## Example Representation
```json
{
    "class": ["collection", "programmes"],
    "entities": [
        {
            "rel": ["/rel/programme"],
            "class": ["programme"],
            "properties": {
                "programmeId": 1,
                "name": "licenciatura eng. inf.",
                "acronym": "LEIC"
            },
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1" }
            ]
        },
        {
            "rel": ["/rel/programme"],
            "class": ["programme"],
            "properties": {
                "programmeId": 2,
                "name": "mestrado eng. inf.",
                "acronym": "MEIC"
            },
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/2" }
            ]
        }
    ],
    "links": [
        { "rel": ["self"], "href": "/v0/programmes?page=0&limit=10" },
        { "rel": ["next"], "href": "/v0/programmes?page=1&limit=10" }
    ]
}
```

---------------------------------------------------------------------

# Programme

A programme represents a type of graduation such as LEIC or MEIC. 
A programme is composed by a set of _offers_. 

## Properties
   * Id
      - MANDATORY
      - type: number
      - Identifies uniquely a Programme
   * Name   
      -  type: text
      -  e.g. "Licenciatura em Engenharia Informática e de Computadores"
    
   * Acronym: programme's acronym
      - mandatory
      - type: text
      - e.g. "LEIC"

   * Term Size: Duration in terms
      - mandatory
      - type: number
      - e.g. 6 terms for LEIC, 4 terms for MEIC


## Link Relations
   * A programme representation
      - must include a link to its context, using the self link relation.
      - must include a link to the collection it belongs, using the `/rel/programmes` relation.
      - must include a link to its _offers_, using the `/rel/offers` relation.

## Example Representation
```json
{
    "class": ["programme"],
    "properties": {
        "id": 1,
        "name": "licenciatura eng. inf.",
        "acronym": "LEIC",
        "termSize": 6
    },
    "links": [
        { "rel": ["self"], "href": "/v0/programmes/1"},
        { "rel": ["/rel/programmes"], "href": "/v0/programmes?page=0&limit=0" },
        { "rel": ["/rel/offers"], "href": "/v0/programmes/1/offers?page=0&limit=0" }
    ]
}
```

# Programme Offer Collection

The Programme Offer collection describes the available set of offers for a specified Programme.

## Properties
   * programmeId
      - MANDATORY
      - type: number
      - Identifies the programme

## Link Relations
   * A programme representation
      - must include a link to its context, using the self link relation.
      - must include a link to the next page
      - may include a link to the previous page

## Example Representation

```json
{
    "class": ["collection", "offers"],
    "properties": {
        "programmeId": 1
    },
    "entities": [
        {
            "rel": ["/rel/offer"],
            "properties": {
                "id": 1,
                "name": "Web Applications Development",
                "courseId": 2,
                "termNumber": [6, 4]
            },
            "title": "Web Applications Development",
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1/offers/1" }
            ]
        },
        {
            "rel": ["/rel/offer"],
            "properties": {
                "id": 2,
                "name": "Software Laboratory",
                "courseId": 1,
                "termNumber": [6]
            },
            "title": "Software Laboratory",
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1/offers/2" }
            ]
        },
        {
            "rel": ["/rel/offer"],
            "properties": {
                "id": 3,
                "name": "Discrete Mathematics",
                "courseId": 3,
                "termNumber": [1]
            },
            "title": "Discrete Mathematics",
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1/offers/3" }
            ]
        },
        {
            "rel": ["/rel/offer"],
            "properties": {
                "id": 4,
                "name": "Project and Seminary",
                "courseId": 4,
                "termNumber": [6]
            },
            "title": "Project and Seminary",
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1/offers/4" }
            ]
        },
        {
            "rel": ["/rel/offer"],
            "properties": {
                "id": 5,
                "name": "Cloud computing",
                "courseId": 5,
                "termNumber": [6]
            },
            "title": "Cloud computing",
            "links": [
                { "rel": ["self"], "href": "/v0/programmes/1/offers/5" }
            ]
        }
    ],
    "links": [
        { "rel": ["self"], "href": "/v0/programmes/1/offers?page=0&limit=10" },
        { "rel": ["next"], "href": "/v0/programmes/1/offers?page=1&limit=10" },
        { "rel": ["/rel/programme"], "href": "/v0/programmes/1" }
    ]
}
```

---------------------------------------------------------------------

# Programme Offer
   A Programme Offer defines a Curricular Unit/Course that can be taken, in the context of a Programme Offer.
   A Programme Offer may have a set of pre-conditions, for example the curricularTerm set where the offer is available or the curricularUnits that precede it.

# Properties
   * Id
      - MANDATORY
      - type: number
      - Uniquely identifies a ProgrammeOffer
      
   * Acronym: the curricular unit acronym
      - mandatory
      - type: text
      - e.g. "LS"

   * Term Number: the term that this offer is available
      - mandatory
      - type: number

   * Optional: If the curricular unit is optional or not
      - mandatory
      - type: boolean  

## Link Relations
   A programme representation:
       * must include a link to its context, using the self link relation.
       * must include a link to the programme offers, using the `/rel/offers` relation.
       * must include a link to the programme, using the `/rel/programme` relation.

## Example representation
```json
{
    "class": ["offer"],
    "properties": {
        "id": 1,
        "name": "Web Applications Development",
        "acronym": "WAD",
        "termNumber": [4, 6],
        "optional": true
    },
    "entities": [
        {
            "rel": ["/rel/course"],
            "class": ["course"],
            "properties": {
                "id": 2,
                "name": "Web Applications Development",
                "acronym": "WAD"
            },
            "links": [
                { "rel": ["self"], "href": "/v0/courses/2" }
            ]
        }
    ],
    "links": [
        { "rel": ["self"],"href": "/v0/programmes/1/offers/1" },
        { "rel": ["/rel/offers"], "href": "/v0/programmes/1/offers?page=0&limit=0" }
    ]
}
```

