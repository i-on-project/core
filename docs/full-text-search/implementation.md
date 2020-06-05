- [Querying multiple tables](#querying-multiple-tables)
- [Format](#format)

# Querying multiple tables

The way we can query multiple tables is by using the UNION keyword to avoid multiple roundtrips.

```sql
set query '''Soft:*'''

SELECT 
    'course' as type,
    id,
    name
FROM dbo.course
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query) 
UNION
SELECT
    'programme' as type,
    id,
    name
FROM dbo.programme
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query);
  type  | id |        name
--------+----+---------------------
 course |  1 | Software Laboratory
(1 row)

SELECT 
    2 as type,
    id,
    name
FROM dbo.course
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query) 
UNION
SELECT
    1 as type,
    id,
    name
FROM dbo.programme
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query);
 type | id |        name
------+----+---------------------
    2 |  1 | Software Laboratory
(1 row)
```

To differenciate between the multiple entities a `type` column was added. This column could have a VARCHAR type like in the 1st example or an integer type like in the 2nd one that can be mapped to a string equivalent to the 1st example.

# Format

The output format of a search for `Lab` could be something along the lines of

```json
{
    "class": [ "search", "result","collection" ],
    "entities": [
        {
            "class": [ "course" ],
            "properties": {
                "id": 1,
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
                "id": 4,
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
                "id": 56,
                "name": "Hardware Laboratory 1920i"
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
                "id": 5,
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
                "id": 545,
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

Potential problems:
* The use of `UNION` means that the same number of columns must be retrieved per `SELECT` and as a consequence it might be hard to create the necessary `href` without multiple round trips
  * Solution:
    * Not search for `class` and `classSection` entities
    * Multiple round trips
    * Have a `href` column that is generated based on each row

```sql
SET query '''Soft:*'''

SELECT
    'class' as type,
    cls.id,
    crs.name || ' ' || calendarterm as name,
    '/v0/courses/' || crs.id || '/classes/' || cls.calendarterm as href
FROM dbo.class cls 
JOIN dbo.course crs ON cls.courseid=crs.id
WHERE to_tsvector(crs.name || ' ' || crs.acronym || ' ' || cls.calendarterm) @@ to_tsquery(:query) 
UNION
SELECT
    'course' as type,
    id,
    name,
    '/v0/courses/' || id as href
FROM dbo.course
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query)
UNION
SELECT
    'programme' as type,
    id,
    name,
    '/vo/programmes/' || id as href 
FROM dbo.programme
WHERE to_tsvector(name || ' ' || acronym) @@ to_tsquery(:query);

  type  | id |           name            |            href
--------+----+---------------------------+-----------------------------
 class  | 22 | Software Laboratory 2021i | /v0/courses/1/classes/2021i
 class  |  4 | Software Laboratory 1718i | /v0/courses/1/classes/1718i
 class  | 10 | Software Laboratory 1819i | /v0/courses/1/classes/1819i
 course |  1 | Software Laboratory       | /v0/courses/1
 class  |  1 | Software Laboratory 1718v | /v0/courses/1/classes/1718v
 class  | 13 | Software Laboratory 1920v | /v0/courses/1/classes/1920v
 class  | 19 | Software Laboratory 2021v | /v0/courses/1/classes/2021v
 class  | 16 | Software Laboratory 1920i | /v0/courses/1/classes/1920i
 class  |  7 | Software Laboratory 1819v | /v0/courses/1/classes/1819v
(9 rows)
```

This way, a fixed search query would need to be created for each searchable table and the result of that query would need to have an agreed set of columns like `type`, `id`, `name` and `href` like in the example. Now knowing every searchable table and the respective queries we can do a database wide search by using `UNION`. If only a subset of tables is searched then simply use that subset's queries.

Example:
There are 4 tables in our database: A, B, C and D.
It is agreed that the result of the text search query should 3 columns: `type CHAR(1)` , `id INTEGER`, `name VARCHAR(256)`.
A search query for table A is defined like
```sql
SELECT
    'A' as type,
    id,
    acronym as name
FROM A
WHERE to_tsvector(acronym) @@ to_tsquery(:query) -- this assumes a variable containing the desired query
```

and others are defined for B, C and D.
If a search is now down for all tables the result of the search would be the result of
```sql
<A query>
UNION
<B query>
UNION
<C query>
UNION
<D query>
```
and if the search was only on the A and C tables it would be the result of
```sql
<A query>
UNION
<C query>
```