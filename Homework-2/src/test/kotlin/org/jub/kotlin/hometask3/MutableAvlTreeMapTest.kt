package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MutableAvlTreeMapTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun clear() {
        val values = getSetOfRandomValues()
        val avl: MutableAvlTreeMap<Int, String> = getMutableAvlTreeMap(values.withStrings)
        avl.clear()
        assertTrue(avl.isEmpty())
        assertTrue(avl.size == 0)
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun remove() {
        val values = getSetOfRandomValues()
        val avl: MutableAvlTreeMap<Int, String> = getMutableAvlTreeMap(values.withStrings)
        var expectedSize = values.size
        values.forEach {
            avl.remove(it)
            expectedSize -= 1
            assertEquals(expectedSize, avl.size)
        }
        assertTrue(avl.isEmpty())
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun put() {
        val values = getSetOfRandomValues()
        val avl: MutableAvlTreeMap<Int, String> = getMutableAvlTreeMap(emptyList())
        assertTrue(avl.isEmpty())
        var expectedSize = 0
        values.forEach {
            val old = avl.put(it, it.toString())
            expectedSize += 1
            assertEquals(expectedSize, avl.size)
            assertNull(old)
        }
        values.forEach {
            val old = avl.put(it, it.toString())
            expectedSize += 1
            assertEquals(it.toString(), old)
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun merge() {
        val values = getSetOfRandomValues(TEST_SET_SIZE).toList()
        val (less, more) = values.partition { it <= values.first() }
        val avl1 = getMutableAvlTreeMap(less.withDoubles)
        val avl2 = getMutableAvlTreeMap(more.withDoubles)
        val avl3 = avl1 mergeWith avl2
        assertEquals(values.size, avl3.size)
        values.forEach {
            assertTrue(avl3.containsKey(it))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mergeThrows() {
        val values = getSetOfRandomValues(TEST_SET_SIZE).toList()
        val (less, more) = values.partition { it <= values.first() }
        val avl1 = getMutableAvlTreeMap(less.withDoubles)
        val avl2 = getMutableAvlTreeMap(more.withDoubles)
        if (less.isEmpty() || more.isEmpty()) return
        assertThrows<Exception>("Illegal merge should throw") {
            avl2 mergeWith avl1
        }
    }

    @Test
    fun merge1() {
        val avl1: MutableAvlTreeMap<Base, Int> = getMutableAvlTreeMap(listOf(Child1(23) withValue 42))
        val avl2 = getMutableAvlTreeMap(listOf(Child2(23) withValue 42))
        avl1 mergeWith avl2
    }

    @Test
    fun merge2() {
        val avl1: MutableAvlTreeMap<Int, Base> = getMutableAvlTreeMap(listOf(42 to Child1(23)))
        val avl2 = getMutableAvlTreeMap(listOf(45 to Child2(23)))
        avl1 mergeWith avl2
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mutableEntries() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val entries = avl.entries
        val entrIterator = entries.iterator()
        while (entrIterator.hasNext()) {
            val entry = entrIterator.next()
            assertTrue(avl.contains(entry.key))
            entrIterator.remove()
            assertFalse(avl.contains(entry.key))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mutableEntries2() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val entries = avl.entries
        val entrIterator = entries.iterator()
        while (entrIterator.hasNext()) {
            val entry = entrIterator.next()
            assertTrue(avl.contains(entry.key))
            entrIterator.remove()
            assertFalse(avl.contains(entry.key))
        }
    }
}
