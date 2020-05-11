package org.ionproject.core.common

import org.jdbi.v3.core.statement.Query

class SQLQueryBuilder {

    private val queryComponents = mutableListOf<String>()

    enum class Order {
        ASC,
        DESC
    }

    fun with(query: Query) : With =
        With().apply {
            with(query)
        }

    fun with(query: String) : With =
        With().apply {
            with(query)
        }

    fun select(vararg columns: String): From = Select().select(*columns)
    fun selectDistinct(vararg columns: String): From = Select().selectDistinct(*columns)

    inner class With : Select() {
        fun with(query: Query) : With {
            queryComponents.add(query.toString())
            return this
        }

        fun with(query: String) : With {
            queryComponents.add(query)
            return this
        }
    }

    open inner class Select {
        fun select(vararg columns: String): From {
            queryComponents.add("select ${columns.joinToString()}")
            return From()
        }

        fun selectDistinct(vararg columns: String): From {
            queryComponents.add("select distinct ${columns.joinToString()}")
            return From()
        }
    }

    open inner class From {
        fun from(table: String) : Join {
            queryComponents.add("from $table\n")
            return Join()
        }
    }

    open inner class Join : Where() {
        fun join(table: String, on: Pair<String, String>) : Join {
            queryComponents.add("join $table on ${on.first} = ${on.second}\n")
            return this
        }
    }

    open inner class Where : GroupBy() {
        private var first = true

        fun where(condition: String): Where {
            queryComponents.add(
                if(first){
                    first = false
                    "where $condition\n"
                } else
                    "and $condition\n"
            )
            return this
        }
    }

    open inner class GroupBy: Having() {
        fun groupBy(vararg columns: String) : Having {
            queryComponents.add("group by ${columns.joinToString()}")
            return this
        }
    }

    open inner class Having : OrderBy() {
        override fun orderBy(column: String, order: Order): OrderBy {
            queryComponents.add("order by $column $order\n")
            return this
        }

        fun having(condition: String) : OrderBy {
            queryComponents.add("having $condition\n")
            return this
        }
    }

    open inner class OrderBy : Limit() {
        private var first = true

        open fun orderBy(column: String, order: Order = Order.ASC) : OrderBy {
            queryComponents.add(", $column $order\n")
            return this
        }
    }

    open inner class Limit : Offset() {
        fun limit(count: Int): Offset {
            queryComponents.add("limit $count\n")
            return this
        }
    }

    open inner class Offset : Builder() {
        fun offset(count: Int): Builder {
            queryComponents.add("offset $count\n")
            return this
        }
    }

    open inner class Builder {
        fun build() : String {
            return queryComponents.reduce { acc, s ->
                acc + s
            }
        }
    }
}