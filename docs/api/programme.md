# Programme

A programme represents a type of graduation such as LEIC or MEIC. 
A programme is composed by a set of _offers_. 

## Properties
   * Id
      - MANDATORY
      - type: number
      - Identifies uniquely a Programme
   * Name   
      -  OPTIONAL (it may not appear in this phase)
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
      - may include a link to the collection it belongs, using the collection link relation.
      - must include a link to its _offers_.

## Actions
    * edit: edit's a programme
    * add Offer: add's a curricular unit offer to the programme

## Example representation
```json
{
    "class": [ "programme" ],
    "properties": {
        "id": 1,
        "name": "Licenciatura em Engenharia Informática e de Computadores", 
        "acronym": "LEIC",
        "termSize": 6
    },
    "entities": [
        {
            "class": [ "offer" ],
            "title": "LS Offer",
            "rel": [ "/rel/programmeOffer" ],
            "properties": {
                "courseId": 2,
                "termNumber": 3
            },
            "links" : [
                { "rel": [ "self" ], "href": "/v0/programmes/1/offers/1"}
            ]
        },
        {
            "class": [ "offer" ],
            "title": "AED Offer",
            "rel": [ "/rel/programmeOffer" ],
            "properties": {
                "CourseId": 5,
                "TermNumber": 3
            },
            "links" : [
                { "rel": [ "self" ], "href": "/v0/programmes/1/offers/2"}
            ]
        },
        {
            "class": [ "offer" ],
            "title": "POO Offer",
            "rel": [ "/rel/programmeOffer" ],
            "properties": {
                "CourseId": 4,
                "TermNumber": 1
            },
            "links" : [
                { "rel": [ "self" ], "href": "/v0/programmes/1/offers/3"}
            ]
        }
    ],
    "actions": [
        {
            "name": "edit-programme",
            "title": "Edit Programme",
            "method": "PUT",
            "href": "/v0/programmes/1",
            "type": "application/json",
            "fields": [
                { "name": "ProgrammeName", "type": "text"}, 
                { "name": "Acronym", "type": "text"},
                { "name": "TermSize", "type": "number"}
            ]
        },
                {
            "name": "add-offer",
            "title": "Add Offer",
            "method": "POST",
            "href": "/v0/programmes/1/offers",
            "type": "application/json",
            "fields": [
                { "name": "CourseId", "type": "number"},
                { "name": "CurricularTerm", "type": "number" },
                { "name": "Optional", "type": "boolean"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/programmes/1" },
        { "rel": [ "up" ], "href": "/v0/programmes/" }
    ]
}
```

---------------------------------------------------------------------
# Programme Collection
A collection of the possible programmes.

## Link Relations
   * A programme representation
    - must include a link to its context, using the self link relation.
    - may include links to the details of a programme, using the item link relation.

## Actions
    * add: add's a programme

## Example representation
```json
{
    "class": [ "collection", "programme" ],
    "entities": [
        {
            "class": [ "Programme" ],
            "rel": [ "item" ],
            "properties": {
                "programmeId": 1,
                "acronym": "LEIC"
            },
            "links" : [
                { "rel": [ "self" ], "href": "/v0/programmes/1" }
            ]
        },
        {
            "class": [ "Programme" ],
            "rel": [ "item" ],
            "properties": {
                "id": 2,
                "acronym": "MEIC"
            },
            "links" : [
                { "rel": [ "self" ], "href": "/v0/programmes/2" }
            ]
        }
    ],
    "actions": [
        {
            "name": "add-programme",
            "title": "Add Programme",
            "method": "POST",
            "href": "/v0/programmes/",
            "type": "application/json",
            "fields": [
                { "name": "ProgrammeAcr", "type": "text"},
                { "name": "TermSize", "type": "number"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/programmes/" }
    ]
}
```


---------------------------------------------------------------------

# ProgrammeOffer
   A ProgrammeOffer defines a CurricularUnit/Course that can be taken, in the context of a ProgrammeOffer.
   A ProgrammeOffer may have a set of pre-conditions, for example the curricularTerm set where the offer is available or the curricularUnits that precede it.

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
       * must include a link to the programme it belongs, using the up link relation.
       * must include a link to the curricular unit details.

## Actions
    * edit: edit's the offer

## Example representation
```json
{
    "class": [ "offer" ],
    "properties": {
        "id": 1,
        "acronym": "LS",
        "termNumber": 2,
        "optional": "true"
    },
    "entities": [
      {
          "class": [ "course" ],
          "rel": [ "/rel/course/" ],
          "links": [
              {"rel": [ "self" ], "href": "/v0/courses/1"}
          ]
       }
    ],
    "actions": [
        {
            "name": "edit",
            "title": "Edit Offer",
            "method": "PUT",
            "type": "application/json",
            "href": "/v0/programmes/1/offers/1",
            "fields": [
                { "name": "Acronym", "type": "text"},
                { "name": "TermNumber", "type": "number"},
                { "name": "Optional", "type": "boolean"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/programmes/1/offers/1" }
        { "rel": [ "related" ], "href": "/v0/programmes/1" }
    ]
}
```

