package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.math.floor
import kotlin.math.log
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BstTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun maximumKey() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTree<Int, Double> = getBst(values.withDoubles)
        assertEquals(values.max(), bst.maximumKey(), "Wrong max key")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minimumKey() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTree<Int, Double> = getBst(values.withDoubles)
        assertEquals(values.min(), bst.minimumKey(), "Wrong min key")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun maximumValue() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTree<Int, Double> = getBst(values.withDoubles)
        assertEquals(values.max().toDouble(), bst.maximumValue(), "Wrong max value")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minimumValue() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTree<Int, Double> = getBst(values.withDoubles)
        assertEquals(values.min().toDouble(), bst.minimumValue(), "Wrong min value")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minmaxThrows() {
        val bst: BalancedSearchTree<Int, Double> = getBst(emptyList())
        assertThrows<Exception>("Empty tree should throw") {
            bst.minimumValue()
        }
        assertThrows<Exception>("Empty tree should throw") {
            bst.maximumValue()
        }
        assertThrows<Exception>("Empty tree should throw") {
            bst.minimumKey()
        }
        assertThrows<Exception>("Empty tree should throw") {
            bst.maximumKey()
        }
    }

    private fun log2(size: Int) = log(size.toDouble(), 2.0)

    @Test
    fun getHeight() {
        for (size in 50..2050 step 100) {
            val values = getSetOfRandomValues(size)
            val bst: BalancedSearchTree<Int, Double> = getBst(values.withDoubles)
            println(bst.height)
            println(size)
            assertTrue(bst.height > floor(log2(size)))
            assertTrue(bst.height < log2(size) * log2(size))
        }
    }
}
