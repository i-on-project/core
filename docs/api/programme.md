# Programme

A programme represents a type of graduation such as LEIC or MEIC. 
A programme is composed by a set of _offers_. 

## Properties
   * Name
      -  mandatory: the programme's name
      -  type: text
      -  e.g. "Licenciatura em Engenharia Informática e de Computadores"
    
   * Acronym: programme's acronym
      - mandatory
      - type: text
      - e.g. "LEIC"

   * Term Size: Duration in terms
      - mandatory
      - type: Integer
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
        "name": "Licenciatura em Engenharia Informática e de Computadores",
        "acronym": "LEIC",
        "termSize": 4
    },
    "entities": [
        {
            "class": [ "ProgrammeOffer" ],
            "title": "LS Offer",
            "rel": [ "/rel/programmeOffer" ],
            "href": "/v0/programmes/LEIC/offers/LS"
        },
        {
            "class": [ "ProgrammeOffer" ],
            "title": "AED Offer",
            "rel": [ "/rel/programmeOffer" ],
            "href": "/v0/programmes/LEIC/offers/AED"
        },
        {
            "class": [ "ProgrammeOffer" ],
            "title": "POO Offer",
            "rel": [ "/rel/programmeOffer" ],
            "href": "/v0/programmes/LEIC/offers/POO"
        }
    ],
    "actions": [
        {
            "name": "edit-programme",
            "title": "edits a programme",
            "method": "PUT",
            "href": "/v0/programmes/LEIC",
            "type": "application/json",
            "fields": [
                { "name": "ProgrammeName", "type": "text"},
                { "name": "acronym", "type": "text"},
                { "name": "termSize", "type": "number"}
            ]
        },
                {
            "name": "add-offer",
            "title": "Add Offer",
            "method": "POST",
            "href": "/v0/programmes/LEIC/offer",
            "type": "application/json",
            "fields": [
                { "name": "CourseAcronym", "type": "text"},
                { "name": "CurricularTerm", "type": "Integer" },
                { "name": "Precedents", "type": "List" }
                { "name": "Optional", "type": "boolean"}
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/programmes/LEIC" },
        { "rel": [ "up" ], "href": "/v0/programmes/" }
    ]
}
```
## Notes: 
   The type List of field Precedents on action "add-offer",
   is not a well defined html type but one adopted for this representation.
   In the context of its use it represents a collection of precedent 
   curricular units to the current offer.
   Example: LS has as precedent SI1 and POO, the same can be represented
   with a list like "SI1,POO".
   Same for type boolean, even thought its not a defined html input type it suits better this case.

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
            "href": "/v0/programmes/LEIC"
        },
        {
            "class": [ "Programme" ],
            "rel": [ "item" ],
            "href": "/v0/programmes/MEIC"
        }
    ],
    "actions": [
        {
            "name": "add-programme",
            "title": "add's a programme",
            "method": "POST",
            "href": "/v0/programmes/",
            "type": "application/json",
            "fields": [
                { "name": "ProgrammeName", "type": "text"},
                { "name": "acronym", "type": "text"},
                { "name": "termSize", "type": "number"}
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
   * Acronym
      - mandatory: the curricular unit acronym offered
      - type: text
      - e.g. "LS"

   * Term Number: the starting term when the curricular unit can be taken.
      - mandatory
      - type: integer

   * Credits: Duration in terms
      - mandatory
      - type: Integer

   * Optional: If the curricular unit is optional or not
      - mandatory
      - type: boolean  

   * Precedents: The curricular units that must be taken before it.
      - mandatory
      - type: List (check Programme notes)
     e.g. "POO, SI1"
     Note: The precedent list gets the next depth of dependencies
     and not the full dependencies tree. (e.g. LS is dependent of POO but POO is dependant of PG)

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
        "credits": 6,
        "optional": "true",
        "precedents": "SI1,POO"
    },
    "entities": [
        {
            "class": [ "course" ],
            "rel": [ "/rels/course" ],
            "href": "/v0/courses/SI1"
        },
        {
            "class": [ "course" ],
            "rel": [ "/rels/course" ],
            "href": "/v0/courses/POO"
        }
    ],
    "actions": [
        {
            "name": "edit",
            "title": "Edit Offer",
            "method": "PUT",
            "type": "application/json",
            "href": "/v0/courses/SI1",
            "fields": [
                { "name": "acronym", "type": "text"},
                { "name": "termNumber", "type": "integer"},
                { "name": "credits", "type": "integer"},
                { "name": "optional", "type": "boolean"},
                { "name": "precedents", "type": "list"},
            ]
        }
    ],
    "links": [
        { "rel": [ "self" ], "href": "/v0/programmes/LEIC/offer/1" }
        { "rel": [ "related" ], "href": "/v0/programmes/LEIC" }
    ]
}
```

