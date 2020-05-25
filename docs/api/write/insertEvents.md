This document informs the clients on how to insert events for Class or Class Section resources.

# HTTP

# Message Format
* usually properties are property-value pairs, in which the value has a defined type/format
    - e.g. `beginTime: 14:00:00`
* *object* typed properties may have a variable number of properties, depending on weather sufficient information was extracted from external sources
    - e.g. `school: { name: "Instituto superior...", acr: "ISEL" }`
    - if one of these properties is missing the message is still acceptable
    - `school`'s properties and types must be defined in this specification

## Properties
### `school`
* *object* type
* `name`
    - *string* type
    - extensive name of
    - e.g. `Instituto Superior de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym 
    - e.g. `ISEL`
* **required**
    - at least one of `name` or `acr` must be included in the message

### `programme`
* *object* type
* `name`
    - *string* type
    - extensive name of 
    - e.g. `Licenciatura de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym
    - e.g. `LEIC`
* **required**
    - at least one of `name` or `acr` must be included in the message

### `calendarSection`
* *string* type
* e.g. `LI11D`
* **optional**
    - the timetable information will be applied to the whole `class` if this property is not present

### `courses`
* *array[object]* type
* `name`
    - *string* type
    - extensive name of 
    - e.g. `Algebra Linear e Geometria Analitica`
* `acr`
    - *string* type
    - acronym 
    - e.g. `ALGA`
* `timetable`
    - *array[object]* type
    - timetable information to be stored in the Core, type defined later on
    - size: **0..N**
* **required**
    - at least one of `name` or `acr` must be included in the message
    - `timetable` must exist but may be empty, in which case the Core will attempt to store the rest of the received properties

### `timetable`
* *object* type
* `type`
    - *char* type
    - from `[a-z]`
    - indicator of the type of event
    - e.g. `t`
* `location`
    - *string* type
    - e.g. `A.2.1`
    - this value does not have a strict format, since other languages may require the use of special characters for identifying locations (e.g. Mandarin Chinese)
* `beginTime`
    - *time* type
* `endTime`
    - *time* type
* `duration`
    - *time* type
* `weekday`
    - *array[string]*
    - must be one of the following: `Mon`, `Tue`, `Wed`, `Thu`, `Fri`, `Sat`, `Sun`
    - size: **1..7**

## Example message body
```
{
    "school": {
        "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
        "acr": "ISEL"
    },
    "programme": {
        "name": "Licenciatura Engenharia Informática e Computadores"
    },
    "calendarTerm": "1718v",
    "calendarSection": "LI11D",
    "courses": [
        {
            "acr": "ALGA[I]",
            "timetable": [
                {
                    "type": "t",
                    "location": "E.1.31",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Monday" ]
                }
            ]
        },
        {
            "acr": "M2[I]",
            "name": "Matemática II",
            "timetable": [
                {
                    "type": "t",
                    "location": "G.2.30",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Tuesday" ]
                },
                {
                    "location": "G.2.30",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Friday" ]
                }
            ]
        }
    ]
}
```

