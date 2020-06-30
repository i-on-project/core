This document informs the clients on how to insert events for `Class Section` resources.

- [HTTP](#http)
  - [Usage](#usage)
- [Message Format](#message-format)
  - [Properties](#properties)
  - [Constants](#constants)
  - [Links](#links)

# HTTP

## Usage
* `PUT /v0/insertClassSectionEvents`
  - this operation is **idempotent**
  - clients need not use hypermedia navigation in order to find this resource's location
  - content type: `application/json`
  - requires authentication `Authorization: Bearer <write token>`

# Message Format

The scope of this section is to define the constraints for the JSON object sent in the HTTP request body.
This is meant to be a more toned down version of the request's [JSON schema]().

Properties are defined as follows:
- the *type* of that property's value
- further constraints may be set on the value itself (e.g. *string* with a minimum of 2 characters)
- for *object*s:
  - the **required** bullets will list out all the mandatory properties for that *object*
  - all properties which are not included in the **required** bullet are optional

## Properties
The following sections will define each property of the JSON object.
All of these properties must be included in the root of the JSON object.
A request which includes additional properties which have not been defined in this document will be rejected (`400`).

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
    - `acr`

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

### `language`
* *string* type
* e.g. `pt-PT`
* consult [#Constants](#constants) for the available values

### `calendarTerm`
* *string* type
* e.g. `1718v`

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
    - `label` (its `acr` property is mandatory)
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
    - *array[string]* type
    - consult [#Constants](#constants) for the available values
    - size: **1..7**
* `category`
    - *integer* type
    - unique category identifier (positive integer)
    - categories are defined and maintained by the Core, so clients must chose the correct identifier based on the available categories
    - consult [#Constants](#constants) for the available values
* **required**
    - `beginTime`, `duration` and `category`

## Constants

Some of the message's properties only have a selected number of values which will be present in this section.

* `category`
  - `1` Exam
  - `2` Lecture
  - `3` Lab
  - `4` Warning
  - `5` Deadline
  - `6` Test

* `weekday`
  - `MO` Monday
  - `TU` Tuesday
  - `WE` Wednesday
  - `TH` Thursday
  - `FR` Friday
  - `SA` Saturday
  - `SU` Sunday

* `language`
  - `pt-PT`
  - `en-US`
  - `en-GB`
  - `ab`
  - `aa`
  - `af`
  - `ak`
  - `sq`
  - `am`
  - `ar`
  - `an`
  - `hy`
  - `as`
  - `av`
  - `ae`
  - `ay`
  - `az`
  - `bm`
  - `ba`
  - `eu`
  - `be`
  - `bn`
  - `bh`
  - `bi`
  - `bs`
  - `br`
  - `bg`
  - `my`
  - `ca`
  - `ch`
  - `ce`
  - `ny`
  - `zh`
  - `zh-Hans`
  - `zh-Hant`
  - `cv`
  - `kw`
  - `co`
  - `cr`
  - `hr`
  - `cs`
  - `da`
  - `dv`
  - `nl`
  - `dz`
  - `eo`
  - `et`
  - `ee`
  - `fo`
  - `fj`
  - `fi`
  - `fr`
  - `ff`
  - `gl`
  - `gd`
  - `gv`
  - `ka`
  - `de`
  - `el`
  - `kl`
  - `gn`
  - `gu`
  - `ht`
  - `ha`
  - `he`
  - `hz`
  - `hi`
  - `ho`
  - `hu`
  - `is`
  - `io`
  - `ig`
  - `id`
  - `in`
  - `ia`
  - `ie`
  - `iu`
  - `ik`
  - `ga`
  - `it`
  - `ja`
  - `jv`
  - `kn`
  - `kr`
  - `ks`
  - `kk`
  - `km`
  - `ki`
  - `rw`
  - `rn`
  - `ky`
  - `kv`
  - `kg`
  - `ko`
  - `ku`
  - `kj`
  - `lo`
  - `la`
  - `lv`
  - `li`
  - `ln`
  - `lt`
  - `lu`
  - `lg`
  - `lb`
  - `mk`
  - `mg`
  - `ms`
  - `ml`
  - `mt`
  - `mi`
  - `mr`
  - `mh`
  - `mo`
  - `mn`
  - `na`
  - `nv`
  - `ng`
  - `nd`
  - `ne`
  - `no`
  - `nb`
  - `nn`
  - `oc`
  - `oj`
  - `cu`
  - `or`
  - `om`
  - `os`
  - `pi`
  - `ps`
  - `fa`
  - `pl`
  - `pa`
  - `qu`
  - `rm`
  - `ro`
  - `ru`
  - `se`
  - `sm`
  - `sg`
  - `sa`
  - `sr`
  - `sh`
  - `st`
  - `tn`
  - `sn`
  - `ii`
  - `sd`
  - `si`
  - `sk`
  - `sl`
  - `so`
  - `nr`
  - `es`
  - `su`
  - `sw`
  - `ss`
  - `sv`
  - `tl`
  - `ty`
  - `tg`
  - `ta`
  - `tt`
  - `te`
  - `th`
  - `bo`
  - `ti`
  - `to`
  - `ts`
  - `tr`
  - `tk`
  - `tw`
  - `ug`
  - `uk`
  - `ur`
  - `uz`
  - `ve`
  - `vi`
  - `vo`
  - `wa`
  - `cy`
  - `wo`
  - `fy`
  - `xh`
  - `yi`
  - `ji`
  - `yo`
  - `za`

## Links
* [JSON Schema](https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/schemas/insertClassSectionEvents.json)
* [Example message](https://github.com/i-on-project/core/blob/docs/gh-123-sketch-write-api-format-doc/docs/api/write/examples/insertClassSectionEvents.json)
