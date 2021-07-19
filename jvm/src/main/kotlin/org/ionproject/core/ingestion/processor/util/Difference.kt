package org.ionproject.core.ingestion.processor.util

typealias EqualityComparator<T, K> = (existent: T, new: K) -> Boolean

class Difference<T, K>(existent: List<T>, new: List<K>, comparator: EqualityComparator<T, K>) {

    private val intersectionList = mutableListOf<Pair<T, K>>()
    private val newSet = new.toMutableSet()
    private val removedList = mutableListOf<T>()

    init {
        existent.forEach { a ->
            val elements = newSet.filter { b -> comparator(a, b) }
            if (elements.isNotEmpty()) {
                intersectionList.addAll(elements.map { elem -> Pair(a, elem) })
                newSet.removeAll(elements)
            } else {
                removedList.add(a)
            }
        }
    }

    val intersection: Collection<Pair<T, K>> = intersectionList
    val newElements: Collection<K> = newSet
    val removedElements: Collection<T> = removedList
}
