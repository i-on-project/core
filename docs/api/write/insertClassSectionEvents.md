This document informs the clients on how to insert events for `Class Section` resources.

# HTTP
## Usage
* `PUT /v0/write/insertClassSectionEvents`
  - this operation is **idempotent**
  - clients need not use hypermedia navigation in order to find this resource's location

# Message Format
The scope of this section is to define the constraints over the JSON objects received as requests for this operation.
This is meant to be a more toned down version of the request's JSON schema.

* each property-value pair is described as follows:
  - the value has a *type*
  - further constraints may be set on the contents of the value itself (e.g. *string* with a minimum of 2 characters)
  - may be **required** or **optional**

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
    - minimum number of characters: 2
    - maximum number of characters: 50
    - e.g. `Instituto Superior de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym 
    - minimum number of characters: 2
    - maximum number of characters: 10
    - e.g. `ISEL`
* **required**
    - at least one of `name` or `acr` must be included in the message

### `programme`
* *object* type
* `name`
    - *string* type
    - extensive name of 
    - minimum number of characters: 2
    - maximum number of characters: 50
    - e.g. `Licenciatura de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym
    - minimum number of characters: 2
    - maximum number of characters: 10
    - e.g. `LEIC`
* **required**
    - at least one of `name` or `acr` must be included in the message

### `calendarSection`
* *string* type
* e.g. `LI11D`
* **required**

### `courses`
* *array[object]* type
* `label`
    - *object* type
    - has the `name` and `acr` properties similar to the `programme` and `school` properties, with the same constraints
    - e.g. `label: { name: "Linear Algebra and Analythic Geometry", acr: "LAAG" }`
* `events`
    - *array[object]* type
    - events to be stored in the Core, type defined later on
    - size: **0..N**
* **required**
    - `label` (at least one of `name` or `acr` must be included in this object)
    - `events` must exist but may be empty, in which case the Core will attempt to store the rest of the received properties

### `event`
* *object* type
* `title`
    - *string* type
    - e.g. `ALGA Lecture`
* `description`
    - *string* type
    - e.g. `ALGA Theorical Lectures`
* `location`
    - *array[string]* type
    - e.g. `[ "A.2.1", "G.0.21 ]`
    - this value does not have a strict format, since other languages may require the use of special characters for identifying locations (e.g. Mandarin Chinese)
* `beginTime`
    - *time* type
    - `hh:mm` format 
    - e.g. `14:00`
* `duration`
    - *time* type
    - `hh:mm` format 
    - e.g. `01:30`
* `weekday`
    - *array[string]*
    - must be one of the following: `MO`, `TU`, `WE`, `TH`, `FR`, `SA`, `SU`
    - size: **1..7**
* **required**
    - `title`, `beginTime` and `duration`
* **optional**
    - `description` and `location`
    - if `weekday` is not present, the `event` will not repeat

## Links
* [JSON Schema](https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/schemas/insertClassSectionEvents.json)
* [Example message](https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/examples/insertClassSectionEvents.json)
