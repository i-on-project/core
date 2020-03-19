**Class**
The following endpoint returns all classes of all calendar terms of given Course,
the following example returns all classes of course 'DAW'.

Parameter size of properties indicates the total number of calendar semesters that the course
DAW was taught. 

Request:
`GET v0/courses/daw/class`

Response:
```
{
    "class" : ["collection", "class"],
    "properties": {
        "course": "DAW",
        "size": 5
    },
    "entities" : [
        {
            "class": ["classDetails"],
            "rel": ["https://example.com/rels/class"],
            "title": "DAW Class of calendar term 1920v",
            "href": "https://example.com/v0/courses/DAW/class/1920v"
        },
        {
            "class": ["classDetails"],
            "rel": ["https://example.com/rels/class"],
            "title": "DAW Class of calendar term 1920v",
            "href": "https://example.com/v0/courses/DAW/class/1920i"
        },
        ...
    ],
    "actions": [],
    "links": [
        { "rel": [ "self" ], "href": "/v0/courses/DAW/class" },
        { "rel": [ "courses" ], "href": "/v0/courses/" }
    ]
}
```

-------------------------------------------------------------------------
Given the diferent Classes a client may want to consume the details of a class at a certain calendar semester.
The response indicates all information of a class at a certain calendar semester like the coordinator , hours, designation...
The response links follow's the graph path, from this point you can check the events of that class or go back and choose a different class.

Request:
`GET v0/courses/daw/class/1920v`

Response:
```
{
    "class" : ["classDetails"],
    "properties": {
        "Course": "DAW",
        "Coordinator": "Pedro Miguel Henriques dos Santos Félix",
        "Calendar term": "1920v",
        "Mandatory": "No",
        "ECTS": "6",
        "Year": "3",
        "CurricularTerm": "6",
        "Hours of work": "162"
        "Programme": "Licenciatura em Engenharia Informática e de Computadores",
        "Designation": "Desenvolvimento de Aplicações Web",
        "Learning Objectives": "...",
        "Learning Content": "..."
    },
    "entities": [
        {
            "class": ["classSection", "collection"],
            "rel": ["https://example.com/rels/classDetails"],
            "title": "ClassSections of Course DAW at Calendar Term 1920v",
            "href": "https://example.com/v0/courses/DAW/class/1920v/classSection"
        }
    ],
    "actions": [],
    "links": [
        { "rel" : [ "self" ], "href": "/v0/courses/DAW/class/1920v" },
        { "rel" : ["class"], "href": "/v0/courses/DAW/class/" },
        { "rel" : [ "events" ], "href": "/v0/courses/DAW/class/1920v/events" }
    ]
}
```
