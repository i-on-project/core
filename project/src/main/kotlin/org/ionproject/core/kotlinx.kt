package org.ionproject.core

import org.ionproject.core.common.querybuilder.Condition
import org.jdbi.v3.core.statement.Query
import org.springframework.util.MultiValueMap

fun CharSequence.toInt(): Int {
    var acc = 0
    val unsigned = this[0] != '-'
    val sequence = if (unsigned) this else this.subSequence(1, this.length)

    sequence.forEach {
        if (it > '9' || it < '0') throw NumberFormatException("$it is not a number.")

        acc *= 10
        acc += it - '0'
    }

    return if (unsigned) acc else acc * -1
}

fun <R> MutableList<R>.fluentAdd(vararg r: R): MutableList<R> {
    this.addAll(r)
    return this
}

fun String.startsAndEndsWith(str: String): Boolean = startsWith(str) && endsWith(str)
fun String.startsAndEndsWith(str: Char): Boolean = startsWith(str) && endsWith(str)

fun String.hexStringToInt() : Int = toInt(16)
fun Int.toHexString() : String = toString(16)

fun Query.bind(queryFilters: Map<String, Condition>, filters: MultiValueMap<String, String>): Query {
    filters.forEach { (key, list) ->
        if (queryFilters.containsKey(key)) {
            list.forEachIndexed { index, s ->
                bind("$key$index", queryFilters[key]?.parse(s))
            }
        }
    }
    return this
}