package org.ionproject.core

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
