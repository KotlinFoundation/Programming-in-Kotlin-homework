package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class MutableIteratorTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun iterate() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = bst.iterator()
        repeat(values.size) {
            assert(iterator.hasNext())
            assertEquals(valIterator.next(), iterator.next().key)
        }
        assertFalse(iterator.hasNext())
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun remove() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = bst.iterator()
        repeat(values.size) {
            assert(iterator.hasNext())
            val currValue = valIterator.next()
            assertEquals(currValue, iterator.next().key)
            iterator.remove()
            assertFalse(bst.contains(currValue))
        }
        assertFalse(iterator.hasNext())
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun callRemoveWithoutNext() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val iterator = bst.iterator()
        assertThrows<IllegalStateException> {
            iterator.remove()
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun callRemoveTwice() {
        val values = getSetOfRandomValues().toList()
        if (values.isEmpty()) return
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val iterator = bst.iterator()
        iterator.next()
        println(bst.size)
        iterator.remove()
        println(bst.size)
        assertEquals(values.size - 1, bst.size)
        assertThrows<IllegalStateException> {
            iterator.remove()
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun invalidate() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val bst: MutableBalancedSearchTreeMap<Int, Double> = getMutableBstMap(values.zip(doubleValues))
        val iterator = bst.iterator()
        iterator.next()
        bst.remove(values.min())
        assertThrows<ConcurrentModificationException> {
            iterator.next()
        }
    }
}
