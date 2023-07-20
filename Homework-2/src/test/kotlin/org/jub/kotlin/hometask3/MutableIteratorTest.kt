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
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = avl.iterator()
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
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = avl.iterator()
        repeat(values.size) {
            assert(iterator.hasNext())
            val currValue = valIterator.next()
            assertEquals(currValue, iterator.next().key)
            iterator.remove()
            assertFalse(avl.contains(currValue))
        }
        assertFalse(iterator.hasNext())
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun callRemoveWithoutNext() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val iterator = avl.iterator()
        assertThrows<IllegalStateException> {
            iterator.remove()
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun callRemoveTwice() {
        val values = getSetOfRandomValues().toList()
        if (values.isEmpty()) return
        val doubleValues = values.map { it.toDouble() }
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val iterator = avl.iterator()
        iterator.next()
        println(avl.size)
        iterator.remove()
        println(avl.size)
        assertEquals(values.size - 1, avl.size)
        assertThrows<IllegalStateException> {
            iterator.remove()
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun invalidate() {
        val values = getSetOfRandomValues().toList()
        if (values.size < 2) return
        val doubleValues = values.map { it.toDouble() }
        val avl: MutableAvlTreeMap<Int, Double> = getMutableAvlTreeMap(values.zip(doubleValues))
        val iterator = avl.iterator()
        iterator.next()
        avl.remove(values.min())
        assertThrows<ConcurrentModificationException> {
            iterator.next()
        }
    }
}
