package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MutableBstMapTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun clear() {
        val values = getSetOfRandomValues()
        val bst: MutableBalancedSearchTreeMap<Int, String> = getMutableBstMap(values.withStrings)
        bst.clear()
        assertTrue(bst.isEmpty())
        assertTrue(bst.size == 0)
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun remove() {
        val values = getSetOfRandomValues()
        val bst: MutableBalancedSearchTreeMap<Int, String> = getMutableBstMap(values.withStrings)
        var expectedSize = values.size
        values.forEach {
            bst.remove(it)
            expectedSize -= 1
            assertEquals(expectedSize, bst.size)
        }
        assertTrue(bst.isEmpty())
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun put() {
        val values = getSetOfRandomValues()
        val bst: MutableBalancedSearchTreeMap<Int, String> = getMutableBstMap(emptyList())
        assertTrue(bst.isEmpty())
        var expectedSize = 0
        values.forEach {
            val old = bst.put(it, it.toString())
            expectedSize += 1
            assertEquals(expectedSize, bst.size)
            assertNull(old)
        }
        values.forEach {
            val old = bst.put(it, it.toString())
            expectedSize += 1
            assertEquals(it.toString(), old)
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun merge() {
        val values = getSetOfRandomValues(TEST_SET_SIZE).toList()
        val (less, more) = values.partition { it <= values.first() }
        val bst1 = getMutableBstMap(less.withDoubles)
        val bst2 = getMutableBstMap(more.withDoubles)
        val bst3 = bst1 mergeWith bst2
        assertEquals(values.size, bst3.size)
        values.forEach {
            assertTrue(bst3.containsKey(it))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mergeThrows() {
        val values = getSetOfRandomValues(TEST_SET_SIZE).toList()
        val (less, more) = values.partition { it <= values.first() }
        val bst1 = getMutableBstMap(less.withDoubles)
        val bst2 = getMutableBstMap(more.withDoubles)
        if (less.isEmpty() || more.isEmpty()) return
        assertThrows<Exception>("Illegal merge should throw") {
            bst2 mergeWith bst1
        }
    }

    @Test
    fun merge1() {
        val bst1: MutableBalancedSearchTreeMap<Base, Int> = getMutableBstMap(listOf(Child1(23) withValue 42))
        val bst2 = getMutableBstMap(listOf(Child2(23) withValue 42))
        bst1 mergeWith bst2
    }

    @Test
    fun merge2() {
        val bst1: MutableBalancedSearchTreeMap<Int, Base> = getMutableBstMap(listOf(42 to Child1(23)))
        val bst2 = getMutableBstMap(listOf(45 to Child2(23)))
        bst1 mergeWith bst2
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mutableEntries() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val entries = bst.entries
        val entrIterator = entries.iterator()
        while (entrIterator.hasNext()) {
            val entry = entrIterator.next()
            assertTrue(bst.contains(entry.key))
            entrIterator.remove()
            assertFalse(bst.contains(entry.key))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun mutableEntries2() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val entries = bst.entries
        val entrIterator = entries.iterator()
        while (entrIterator.hasNext()) {
            val entry = entrIterator.next()
            assertTrue(bst.contains(entry.key))
            entrIterator.remove()
            assertFalse(bst.contains(entry.key))
        }
    }
}
