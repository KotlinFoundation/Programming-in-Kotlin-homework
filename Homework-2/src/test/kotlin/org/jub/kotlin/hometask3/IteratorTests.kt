package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class IteratorTests {
    @RepeatedTest(TEST_ITERATIONS)
    fun iterate() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val avl: AvlTreeMap<Int, Double> = getAvlTreeMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = avl.iterator()
        repeat(values.size) {
            assert(iterator.hasNext())
            assertEquals(valIterator.next(), iterator.next().key)
        }
        assertFalse(iterator.hasNext())
    }
}
