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

fun <K, V, NK, NV> Map<K, V>.mapEntries(oper: (Map.Entry<K, V>) -> Pair<NK, NV>): Map<NK, NV> {
    return map {
        oper(it)
    }.associate {
        it
    }
}

fun String.removeWhitespace() =
  replace("\\s".toRegex(), "")

/**
 * [regex] - regular expression used for comparison
 * [count] - how many characters to remove counting from the end
 */
fun String.split(regex: Regex, count: Int = 1, limit: Int = 0): ArrayList<String> {
    val array = arrayListOf<String>()

    var actualLimit = if (limit == 0) length else limit
    var lastIndex = 0
    var result = regex.find(this, lastIndex)

    while (result != null && actualLimit > 0) {
        --actualLimit

        val nextLastIndex = result.range.last + 1

        array.add(this.substring(lastIndex, nextLastIndex - count))

        lastIndex = nextLastIndex

        result = regex.find(this, lastIndex)
    }

    if (actualLimit > 0) array.add(substring(lastIndex))

    return array
}
