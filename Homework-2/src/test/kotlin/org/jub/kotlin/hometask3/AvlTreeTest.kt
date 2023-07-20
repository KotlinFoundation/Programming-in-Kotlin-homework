package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.math.floor
import kotlin.math.log
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AvlTreeTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun maximumKey() {
        val values = getSetOfRandomValues()
        val avl: AvlTree<Int, Double> = getAvlTree(values.withDoubles)
        assertEquals(values.max(), avl.maximumKey(), "Wrong max key")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minimumKey() {
        val values = getSetOfRandomValues()
        val avl: AvlTree<Int, Double> = getAvlTree(values.withDoubles)
        assertEquals(values.min(), avl.minimumKey(), "Wrong min key")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun maximumValue() {
        val values = getSetOfRandomValues()
        val avl: AvlTree<Int, Double> = getAvlTree(values.withDoubles)
        assertEquals(values.max().toDouble(), avl.maximumValue(), "Wrong max value")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minimumValue() {
        val values = getSetOfRandomValues()
        val avl: AvlTree<Int, Double> = getAvlTree(values.withDoubles)
        assertEquals(values.min().toDouble(), avl.minimumValue(), "Wrong min value")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun minmaxThrows() {
        val avl: AvlTree<Int, Double> = getAvlTree(emptyList())
        assertThrows<Exception>("Empty tree should throw") {
            avl.minimumValue()
        }
        assertThrows<Exception>("Empty tree should throw") {
            avl.maximumValue()
        }
        assertThrows<Exception>("Empty tree should throw") {
            avl.minimumKey()
        }
        assertThrows<Exception>("Empty tree should throw") {
            avl.maximumKey()
        }
    }

    private fun log2(size: Int) = log(size.toDouble(), 2.0)

    @Test
    fun getHeight() {
        for (size in 50..2050 step 100) {
            val values = getSetOfRandomValues(size)
            val avl: AvlTree<Int, Double> = getAvlTree(values.withDoubles)
            println(avl.height)
            println(size)
            assertTrue(avl.height > floor(log2(size)))
            assertTrue(avl.height < log2(size) * log2(size))
        }
    }
}
