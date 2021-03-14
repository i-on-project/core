package org.ionproject.core.utils

import org.ionproject.core.common.Action
import org.ionproject.core.common.EmbeddedRepresentation
import org.ionproject.core.common.Field
import org.ionproject.core.common.Relation
import org.ionproject.core.common.Siren
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.fail

fun assertSiren(expected: Siren?, actual: Siren?) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null be it was.")

        assertEquals(expected.klass, actual.klass)

        if (expected.properties != null) {
            assertNotNull(actual.properties, "Expected properties to be not null but they were.")
            assertEquals(expected.properties, actual.properties)
        } else {
            assertNull(actual.properties, "Expected properties to be null but they weren't.")
        }

        assertCollection(expected.entities, actual.entities, ::assertEmbeddedSiren)
        assertEquals(expected.title, actual.title)
        assertCollection(expected.actions, actual.actions, ::assertAction)
        assertCollection(expected.links, actual.links, ::assertRelation)

    } else {
        assertNull(actual, "Expected actual to be null but is wasn't.")
    }
}

fun assertEmbeddedSiren(expected: EmbeddedRepresentation?, actual: EmbeddedRepresentation?) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null be it was.")

        assertEquals(expected.rel, actual.rel)
        assertEquals(expected.klass, actual.klass)

        if (expected.properties != null) {
            assertNotNull(actual.properties, "Expected properties to be not null but they were.")
            assertEquals(actual.properties, expected.properties)
        } else {
            assertNull(actual.properties, "Expected properties to be null but they weren't.")
        }

        assertCollection(expected.entities, actual.entities, ::assertEmbeddedSiren)
        assertEquals(expected.title, actual.title)
        assertCollection(expected.actions, actual.actions, ::assertAction)
        assertCollection(expected.links, actual.links, ::assertRelation)

    } else {
        assertNull(actual, "Expected actual to be null but is wasn't.")
    }
}

fun assertRelation(expected: Relation?, actual: Relation?) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null be it was.")

        assertEquals(expected.href, actual.href)
        assertEquals(expected.klass, actual.klass)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.rel, actual.rel)
    } else {
        assertNull(actual, "Expected actual to be null but is wasn't.")
    }
}

fun assertAction(expected: Action?, actual: Action?) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null be it was.")

        assertEquals(expected.href, actual.href)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.type, actual.type)
        assertEquals(expected.isTemplated, actual.isTemplated)
        assertEquals(expected.method, actual.method)
        assertEquals(expected.name, actual.name)
        assertCollection(expected.fields, actual.fields) { f1, f2 ->
            assertField(f1, f2)
        }
    } else {
        assertNull(actual, "Expected actual to be null but is wasn't.")
    }
}

fun assertField(expected: Field?, actual: Field?) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null be it was.")
        assertEquals(expected.title, actual.title)
        assertEquals(expected.type, actual.type)
        assertEquals(expected.name, actual.name)
        assertEquals(expected.klass, actual.klass)
    } else {
        assertNull(actual, "Expected actual to be null but is wasn't.")
    }
}

fun <T> assertCollection(expected: Collection<T>?, actual: Collection<T>?, itemAsserter: (T, T) -> Unit) {
    if (expected != null) {
        if (actual == null) fail("Expected actual to be not null but it was.")

        assertEquals(expected.size, actual.size)

        val expectedIterator = expected.iterator()
        val actualIterator = actual.iterator()

        while (expectedIterator.hasNext()) {
            if (!actualIterator.hasNext()) fail("The actual collection is shorter.")
            itemAsserter(expectedIterator.next(), actualIterator.next())
        }
        if (actualIterator.hasNext()) fail("The expected collection is shorter.")

    } else {
        assertNull(actual, "Expected actual to be null but it wasn't.")
    }
}