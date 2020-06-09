- [Using a tsvector column](#using-a-tsvector-column)
- [Indexes](#indexes)
  - [GIN indexing](#gin-indexing)
  - [GiST indexing](#gist-indexing)

# Using a tsvector column

To speed things up a column can be added to searchable tables with the document to be searched.

```sql
                                                 Table "public.yahooanswers"
        Column        |  Type  | Collation | Nullable |           Default            | Storage  | Stats target | Description
----------------------+--------+-----------+----------+------------------------------+----------+--------------+-------------
 id                   | bigint |           | not null | generated always as identity | plain    |              |
 question_title       | text   |           | not null |                              | extended |              |
 question_description | text   |           |          |                              | extended |              |
 answer               | text   |           |          |                              | extended |              |

 List of relations
 Schema |     Name     | Type  |  Owner   |  Size  | Description
--------+--------------+-------+----------+--------+-------------
 public | yahooanswers | table | postgres | 543 MB |

SELECT COUNT(*)
FROM yahooanswers;
  count
---------
 1048576
(1 row)



SELECT COUNT(*)
FROM yahooanswers
WHERE to_tsvector(question_title || ' ' || coalesce(question_description, '') || ' ' || coalesce(answer, '')) @@ to_tsquery('Soft:* | Math:*');
 count
-------
 29479
(1 row)


EXPLAIN ANALYZE
SELECT COUNT(*)
FROM yahooanswers
WHERE to_tsvector(question_title || ' ' || coalesce(question_description, '') || ' ' || coalesce(answer, '')) @@ to_tsquery('Soft:* | Math:*');
                                                                                                  QUERY PLAN
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Finalize Aggregate  (cost=344397.20..344397.21 rows=1 width=8) (actual time=42127.776..42127.776 rows=1 loops=1)
   ->  Gather  (cost=344396.99..344397.20 rows=2 width=8) (actual time=42127.573..42137.277 rows=3 loops=1)
         Workers Planned: 2
         Workers Launched: 2
         ->  Partial Aggregate  (cost=343396.99..343397.00 rows=1 width=8) (actual time=42071.088..42071.088 rows=1 loops=3)
               ->  Parallel Seq Scan on yahooanswers  (cost=0.00..343353.73 rows=17302 width=0) (actual time=5.382..42065.665 rows=9826 loops=3)
                     Filter: (to_tsvector(((((question_title || ' '::text) || COALESCE(question_description, ''::text)) || ' '::text) || COALESCE(answer, ''::text))) @@ to_tsquery('Soft:* | Math:*'::text))
                     Rows Removed by Filter: 339699
 Planning Time: 0.088 ms
 Execution Time: 42137.306 ms
(10 rows)


ALTER TABLE yahooanswers ADD COLUMN document TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', question_title || ' ' || coalesce(question_description,'') || ' ' || coalesce(answer, ''))) STORED;

                        List of relations
 Schema |     Name     | Type  |  Owner   |  Size   | Description
--------+--------------+-------+----------+---------+-------------
 public | yahooanswers | table | postgres | 1164 MB |

EXPLAIN ANALYZE
SELECT COUNT(*)
FROM yahooanswers
WHERE document @@ to_tsquery('Soft:* | Math:*');
                                                                   QUERY PLAN
------------------------------------------------------------------------------------------------------------------------------------------------
 Finalize Aggregate  (cost=230801.47..230801.48 rows=1 width=8) (actual time=1751.119..1751.119 rows=1 loops=1)
   ->  Gather  (cost=230801.26..230801.47 rows=2 width=8) (actual time=1750.629..1762.527 rows=3 loops=1)
         Workers Planned: 2
         Workers Launched: 2
         ->  Partial Aggregate  (cost=229801.26..229801.27 rows=1 width=8) (actual time=1688.591..1688.591 rows=1 loops=3)
               ->  Parallel Seq Scan on yahooanswers  (cost=0.00..229758.00 rows=17302 width=0) (actual time=1.431..1686.458 rows=9826 loops=3)
                     Filter: (document @@ to_tsquery('Soft:* | Math:*'::text))
                     Rows Removed by Filter: 339699
 Planning Time: 0.080 ms
 Execution Time: 1762.553 ms
(10 rows)
```

As we can see the use of a `document tsvector` column allows us to look through the table about 24x faster at the cost of doubling the table size.

# Indexes

Now that we have the `document` column it is possible to index it in order to speed even further our searches.

## GIN indexing

A [GIN index](https://www.postgresql.org/docs/current/gin.html) is what is recommended for this type of data and search when the document aren't unique and have random structure. So in the context of our application it would make sense to apply it to `CalendarComponent` `summaries` or `descriptions`.

```sql
CREATE INDEX ON yahooanswers USING GIN (document);

                                      List of relations
 Schema |           Name            | Type  |  Owner   |    Table     |  Size  | Description
--------+---------------------------+-------+----------+--------------+--------+-------------
 public | yahooanswers_document_idx | index | postgres | yahooanswers | 189 MB |

EXPLAIN ANALYZE SELECT COUNT(*) FROM yahooanswers WHERE document @@ to_tsquery('Soft:* | Math:*');
                                                                           QUERY PLAN
----------------------------------------------------------------------------------------------------------------------------------------------------------------
 Finalize Aggregate  (cost=88380.40..88380.41 rows=1 width=8) (actual time=192.069..192.069 rows=1 loops=1)
   ->  Gather  (cost=88380.19..88380.40 rows=2 width=8) (actual time=191.556..200.025 rows=3 loops=1)
         Workers Planned: 2
         Workers Launched: 2
         ->  Partial Aggregate  (cost=87380.19..87380.20 rows=1 width=8) (actual time=131.767..131.767 rows=1 loops=3)
               ->  Parallel Bitmap Heap Scan on yahooanswers  (cost=434.06..87336.93 rows=17302 width=0) (actual time=6.104..130.751 rows=9826 loops=3)
                     Recheck Cond: (document @@ to_tsquery('Soft:* | Math:*'::text))
                     Heap Blocks: exact=15383
                     ->  Bitmap Index Scan on yahooanswers_document_idx  (cost=0.00..423.68 rows=41524 width=0) (actual time=14.918..14.918 rows=29479 loops=1)
                           Index Cond: (document @@ to_tsquery('Soft:* | Math:*'::text))
 Planning Time: 0.491 ms
 Execution Time: 200.315 ms
```

Now our execution time is around 9x faster than before at the cost of approximately 16% increase in storage consumption.

## GiST indexing

A [GiST index](https://www.postgresql.org/docs/current/gist.html) is what is recommended for this type of data and search when the documents are unique. So in the context of our application it would make sense to apply it to `Course's` and `Programme's` `acronyms` and `names`.

```sql
CREATE INDEX ON yahooanswers USING GIST (document);

EXPLAIN ANALYZE SELECT COUNT(*) FROM yahooanswers WHERE document @@ to_tsquery('Soft:* | Math:*');
                                                                             QUERY PLAN
---------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Finalize Aggregate  (cost=92256.82..92256.83 rows=1 width=8) (actual time=2111.317..2111.318 rows=1 loops=1)
   ->  Gather  (cost=92256.60..92256.81 rows=2 width=8) (actual time=2110.244..2123.068 rows=3 loops=1)
         Workers Planned: 2
         Workers Launched: 2
         ->  Partial Aggregate  (cost=91256.60..91256.61 rows=1 width=8) (actual time=2051.605..2051.605 rows=1 loops=3)
               ->  Parallel Bitmap Heap Scan on yahooanswers  (cost=4310.47..91213.35 rows=17302 width=0) (actual time=300.762..2049.509 rows=9826 loops=3)
                     Recheck Cond: (document @@ to_tsquery('Soft:* | Math:*'::text))
                     Rows Removed by Index Recheck: 339699
                     Heap Blocks: exact=16567 lossy=22335
                     ->  Bitmap Index Scan on yahooanswers_document_idx  (cost=0.00..4300.09 rows=41524 width=0) (actual time=349.094..349.094 rows=1048576 loops=1)
                           Index Cond: (document @@ to_tsquery('Soft:* | Math:*'::text))
 Planning Time: 0.526 ms
 Execution Time: 2123.120 ms
```

The dataset used in the example is comprised of documents that favor GIN indexing so we see a decrease in speed.