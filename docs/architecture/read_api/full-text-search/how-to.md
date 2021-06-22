- [1. Introduction](#1-introduction)
- [2. Normalization](#2-normalization)
- [3. Querying multiple columns](#3-querying-multiple-columns)
- [4. Auxiliary functions](#4-auxiliary-functions)
  - [4.1. `to_tsvector`](#41-to_tsvector)
  - [4.2. `to_tsquery`](#42-to_tsquery)
  - [4.3. `plainto_tsquery`](#43-plainto_tsquery)
- [5. Partial word search](#5-partial-word-search)
- [6. Bibliography](#6-bibliography)

# 1. Introduction

PostgreSQL has two datatypes that allow _Full Text Search_: _tsquery_(query) & _tsvector_(document).

_tsquery_ represents what we are searching for.
_tsvector_ represents what we are searching in.

Example _Course_ Table:

| ID | Acronym | Name |
|:---:|:---:|:---:|
| 1 | SL | Software Laboratory |
| 2 | WAD | Web App Development |
| 3 | DM | Discrete Mathematics |

If we wanted to search for courses with `Software` and `Development` in the Name column the queries we would need to make are:

```sql
SELECT * FROM dbo.course WHERE to_tsvector(course.name) @@ plainto_tsquery('Software');
 id | acronym |        name
----+---------+---------------------
  1 | SL      | Software Laboratory
(1 row)

SELECT * FROM dbo.course WHERE to_tsvector(course.name) @@ plainto_tsquery('Development');
 id | acronym |             name
----+---------+------------------------------
  2 | WAD     | Web Applications Development
(1 row)
```

# 2. Normalization

Full text searching in PostgreSQL is based on the match operator @@, which returns true if a _tsvector_  matches a _tsquery_ . It doesn't matter which data type is written first.

The datatype _tsvector_ is a normalization of text and pairing of words with their position. This normalization is done with a [_Dictionary_](https://www.postgresql.org/docs/current/textsearch-dictionaries.html) which removes common words, reduces words down to their root words, such as `development -> develop`.

The following examples show the result of the normalization and vectorization of strings

```sql
SELECT to_tsvector('english', 'First fullstack experience in the programme');
                   to_tsvector
-------------------------------------------------
 'experi':3 'first':1 'fullstack':2 'programm':6
(1 row)
```

```sql
SELECT to_tsvector('Course with fullstack development with Spring MVC backend and React frontend. Optional course in LEIC.');
                                                     to_tsvector
----------------------------------------------------------------------------------------------------------------------
 'backend':8 'cours':1,13 'develop':4 'frontend':11 'fullstack':3 'leic':15 'mvc':7 'option':12 'react':10 'spring':6
(1 row)
```

As the examples show the common words `in`, `with`, `and` were removed and others like `development` and `optional` were, respectively, narrowed down to `develop` and `option`.

The same kind of normalization occurs with _tsquery_.

# 3. Querying multiple columns

The querying of multiple columns is simply done by string concatenation as shown in the [documentation](https://www.postgresql.org/docs/current/textsearch-tables.html#TEXTSEARCH-TABLES-SEARCH).

```sql
SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ plainto_tsquery('Development');
 id | acronym |             name
----+---------+------------------------------
  2 | WAD     | Web Applications Development
(1 row)

SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ plainto_tsquery('WAD Development');
 id | acronym |             name
----+---------+------------------------------
  2 | WAD     | Web Applications Development
(1 row)

SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ plainto_tsquery('WAD Software');
 id | acronym | name
----+---------+------
(0 rows)
```

# 4. Auxiliary functions

Above we have been using a few auxiliary functions such as `to_tsvector`, `to_tsquery` and `plainto_tsquery`. They all take at least a string as input and output the specified type.

## 4.1. `to_tsvector`    

This function receives a configuration and a document(text) and produces a _tsvector_.
```js
to_tsvector('english', 'Course with fullstack development with Spring MVC backend and React frontend. Optional course in LEIC.')
```

Configurations hold information about what _Dictionary_ to use for example. PostgreSQL comes with certain configurations already, but it is possible to make custom ones.

## 4.2. `to_tsquery`

This function receives a string and produces a _tsquery_.
```sql
SELECT to_tsquery('Software');
 to_tsquery
------------
 'softwar'
(1 row)

SELECT to_tsquery('Laboratory');
  to_tsquery
--------------
 'laboratori'
(1 row)

SELECT to_tsquery('Software & Laboratory');
        to_tsquery
--------------------------
 'softwar' & 'laboratori'
(1 row)

SELECT to_tsquery('Software | Laboratory');
        to_tsquery
--------------------------
 'softwar' | 'laboratori'
(1 row)

SELECT to_tsquery('Software Laboratory');
ERROR:  syntax error in tsquery: "Software Laboratory"
```

The string passed is expected to follow certain syntax rules like shown in the 3rd example. `Software & Laboratory` means that this two words must be in the document while `Software | Laboratory` means that only one need to be in it. A description of this data type and the correct syntax to use can be found [here](https://www.postgresql.org/docs/12/datatype-textsearch.html#DATATYPE-TSQUERY).

## 4.3. `plainto_tsquery`

This function receives a string and produces a _tsquery_.
```sql
SELECT plainto_tsquery('Software');
 plainto_tsquery
-----------------
 'softwar'
(1 row)

SELECT plainto_tsquery('Software Laboratory');
     plainto_tsquery
--------------------------
 'softwar' & 'laboratori'
(1 row)

SELECT plainto_tsquery('Software & Laboratory');
     plainto_tsquery
--------------------------
 'softwar' & 'laboratori'
(1 row)

SELECT plainto_tsquery('Software | Laboratory');
     plainto_tsquery
--------------------------
 'softwar' & 'laboratori'
(1 row)
```

In this function, as opposed to [`to_tsquery`](#42-to_tsquery), the string passed is not expected to follow specific syntax rules. Everything is split by whitespace, with some parts being ignored, and the result being the joining of all the parts with the `&` operator.

# 5. Partial word search

Sometimes we want to be able to find a document with a certain word but we are only given its prefix, like having only `dev` when you want to look up `development`. In that case it is possible to _tsqueries_ that do exactly that.

```sql
SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ to_tsquery('dev');
 id | acronym | name
----+---------+------
(0 rows)

SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ to_tsquery('dev:*');
 id | acronym |             name
----+---------+------------------------------
  2 | WAD     | Web Applications Development
(1 row)

SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ to_tsquery('dev:* & app:*');
 id | acronym |             name
----+---------+------------------------------
  2 | WAD     | Web Applications Development
(1 row)

SELECT * FROM dbo.course WHERE to_tsvector(course.acronym || ' ' || course.name) @@ to_tsquery('dev:* | soft:*');
 id | acronym |             name
----+---------+------------------------------
  1 | SL      | Software Laboratory
  2 | WAD     | Web Applications Development
(2 rows)
```

# 6. Bibliography
- ["The State of (Full) Text Search in PostgreSQL 12" by Jimmy Angelakos](https://www.youtube.com/watch?v=c8IrUHV70KQ)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/current/textsearch.html)