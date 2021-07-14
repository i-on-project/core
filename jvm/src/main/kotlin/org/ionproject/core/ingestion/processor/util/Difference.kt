package org.ionproject.core.ingestion.processor.util

typealias EqualityComparator<T, K> = (existent: T, new: K) -> Boolean

class Difference<T, K>(existent: List<T>, new: List<K>, comparator: EqualityComparator<T, K>) {

    private val intersectionList = mutableListOf<Pair<T, K>>()
    private val newSet = new.toMutableSet()
    private val removedList = mutableListOf<T>()

    init {
        // O(n*m) time complexity, where n = size(existent) and m = size(new)
        // This complexity could be reduced to an amortized O(n) if we performed a comparison
        // over the same type (without a custom comparator)
        existent.forEach { a ->
            val elem = newSet.find { b -> comparator(a, b) }
            if (elem != null) {
                intersectionList.add(Pair(a, elem))
            } else {
                removedList.add(a)
            }

            newSet.remove(elem)
        }
    }

    val intersection: Collection<Pair<T, K>> = intersectionList
    val newElements: Collection<K> = newSet
    val removedElements: Collection<T> = removedList
}
