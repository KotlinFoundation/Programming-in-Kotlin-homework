package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class IteratorTests {
    @RepeatedTest(TEST_ITERATIONS)
    fun iterate() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: BalancedSearchTreeMap<Int, Double> = getBstMap(values.zip(doubleValues))
        val sortedValues = values.sorted()
        val valIterator = sortedValues.iterator()
        val iterator = bst.iterator()
        repeat(values.size) {
            assert(iterator.hasNext())
            assertEquals(valIterator.next(), iterator.next().key)
        }
        assertFalse(iterator.hasNext())
    }
}
