This document informs the clients on how to insert faculty information for `Class Section` resources.

- [HTTP](#http)
  - [Usage](#usage)
- [Message Format](#message-format)
  - [Properties](#properties)
  - [Constants](#constants)
  - [Links](#links)

# HTTP

## Usage
* `PUT /v0/insertClassSectionFaculty`
  - this operation is **idempotent**
  - clients need not use hypermedia navigation in order to find this resource's location
  - content type: `application/json`
  - requires authentication `Authorization: Bearer <write token>`

# Message Format

The scope of this section is to define the constraints for the JSON object sent in the HTTP request body.
This is meant to be a more toned down version of the request's [JSON schema](./schemas/insertClassSectionFaculty.json).

Properties are defined as follows:
- the *type* of that property's value
- further constraints may be set on the value itself (e.g. *string* with a minimum of 2 characters)
- for *object*s:
  - the **required** bullets will list out all the mandatory properties for that *object*
  - all properties which are not included in the **required** bullet are optional

## Properties
Each subsection is named after a property which will have to be included in the root of the JSON object, unless it is marked with **optional**.

Properties marked with the quote "consult [#Constants](#constants) for the available values" will have a fixed number of values which clients have to pick from the [#Constants](#constants) section.

### `school`
* *object* type
* `name`
    - *string* type
    - extensive name of
    - minimum number of characters: 2
    - maximum number of characters: 100
    - e.g. `Instituto Superior de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym 
    - minimum number of characters: 1
    - maximum number of characters: 10
    - e.g. `ISEL`
* **required**
    - `acr`

### `programme`
* *object* type
* `name`
    - *string* type
    - extensive name of 
    - minimum number of characters: 1
    - maximum number of characters: 100
    - e.g. `Licenciatura de Engenharia de Lisboa`
* `acr`
    - *string* type
    - acronym
    - minimum number of characters: 1
    - maximum number of characters: 10
    - e.g. `LEIC`
* **required**
    - `acr`

### `calendarSection`
* *string* type
* the unique identifier of a `Class Section`
* e.g. `LI11D`

### `language`
* *string* type
* informs the Core of the language used in text values of this request
* **optional**, if no language is provided the Core will assume all text is in American English `en-US`
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
    - `acr` **required**
    - e.g. `label: { name: "Linear Algebra and Analythic Geometry", acr: "LAAG" }`
* `teachers`
    - *array[object]* type
    - size: **0..N**
* **required**
    - `label` (its `acr` property is mandatory)
    - `teachers` must exist but may be empty, in which case the Core will attempt to store the rest of the received properties

### `teacher`
* *object* type
* `name`
    - *string* type
    - e.g. `Bob McDonald`
* **required**
    - `name`

## Constants

Some of the message's properties only have a selected number of values which will be present in this section.

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
* [JSON schema](./schemas/insertClassSectionFaculty.json)
* [Example message](./examples/insertClassSectionFaculty.json)
