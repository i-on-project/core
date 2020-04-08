package org.ionproject.core

fun <T,R> Iterable<T>.twoPhaseReduce(transformOperation: (input: T) -> R, operation: (acc: R, item: R) -> R) : R {
    val iterator = this.iterator()
    var ret: R
    if (iterator.hasNext()) {
        ret = transformOperation(iterator.next())

        while(iterator.hasNext()) {
            ret = operation(ret, transformOperation(iterator.next()))
        }

        return ret
    }
    throw IllegalStateException("Cannot call twoPhaseReduce on an empty Iterable.")
}

fun <T,R> Array<T>.twoPhaseReduce(transformOperation: (input: T) -> R, operation: (acc: R, item: R) -> R) : R {
    val iterator = this.iterator()
    var ret: R
    if (iterator.hasNext()) {
        ret = transformOperation(iterator.next())

        while(iterator.hasNext()) {
            ret = operation(ret, transformOperation(iterator.next()))
        }

        return ret
    }
    throw IllegalStateException("Cannot call twoPhaseReduce on an empty Iterable.")
}

fun CharSequence.toInt() : Int {
    var acc = 0
    val signal = this[0] != '-'
    val sequence = if(signal) this else this.subSequence(1, this.length)

    sequence.forEach {
        if (it > '9' || it < '0') throw NumberFormatException("$it is not a number.")

        acc *= 10
        acc += it - '0'
    }

    return if (signal) acc else acc * -1
}