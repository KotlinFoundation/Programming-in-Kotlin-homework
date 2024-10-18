package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class BstListTest {
    @RepeatedTest(TEST_ITERATIONS)
    fun contains() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeList<Int, Double> = getBstList(values.withDoubles)
        values.forEach {
            assertTrue(bst.contains(it.toDouble()))
        }
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(bst.contains(it.toDouble()))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun containsAll() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: BalancedSearchTreeList<Int, Double> = getBstList(values.zip(doubleValues))
        assertTrue(bst.containsAll(doubleValues + doubleValues))
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(bst.containsAll(doubleValues + it.toDouble() + doubleValues))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun get() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val bst: BalancedSearchTreeList<Int, Double> = getBstList(values.zip(doubleValues))
        values.sorted().forEachIndexed { index, value ->
            assertEquals(value.toDouble(), bst[index])
        }
    }

    @Suppress("KotlinConstantConditions")
    @RepeatedTest(TEST_ITERATIONS)
    fun getThrows() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeList<Int, Double> = getBstList(values.withDoubles)
        for (i in 0..TEST_SET_SIZE) {
            assertThrows<IndexOutOfBoundsException> {
                bst[bst.size + i]
            }
        }
        for (i in -1 downTo -bst.size) {
            assertThrows<IndexOutOfBoundsException> {
                bst[i]
            }
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun listIterator() {
        val values = getSetOfRandomValues().toList().sorted()
        val bst: BalancedSearchTreeList<Int, Double> = getBstList(values.withDoubles)
        val valuesIterator = values.listIterator()
        val bstIterator = bst.listIterator()
        for (i in values.indices) {
            assertTrue(bstIterator.hasNext())
            assertEquals(valuesIterator.nextIndex(), bstIterator.nextIndex())
            assertEquals(valuesIterator.next().toDouble(), bstIterator.next())
        }
        for (i in values.size downTo values.size.div(2)) {
            assertTrue(bstIterator.hasPrevious())
            assertEquals(valuesIterator.previousIndex(), bstIterator.previousIndex())
            assertEquals(valuesIterator.previous().toDouble(), bstIterator.previous())
        }
        for (i in values.size.div(2)..values.size.div(4)) {
            assertTrue(bstIterator.hasNext())
            assertEquals(valuesIterator.nextIndex(), bstIterator.nextIndex())
            assertEquals(valuesIterator.next().toDouble(), bstIterator.next())
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun lastIndexOf() {
        val valuesSet = getSetOfRandomValues()
        val values = valuesSet.toList()
        val entries = values.flatMap { value -> List(3) { index -> "$value #$index" to value } }.sortedBy { it.first }
        val bst: BalancedSearchTreeList<String, Int> = getBstList(entries.shuffled())
        val encounteredCount = mutableMapOf<Int, Int>()
        entries.forEachIndexed { index, (_, value) ->
            when (val count = encounteredCount.getOrDefault(value, 0)) {
                0, 1 -> {
                    encounteredCount[value] = count + 1
                    assertNotEquals(index, bst.lastIndexOf(value))
                }

                else -> {
                    assertEquals(index, bst.lastIndexOf(value))
                }
            }
        }

        getSetOfRandomValues().filter { it !in values }.forEach {
            assertEquals(-1, bst.lastIndexOf(it))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun indexOf() {
        val valuesSet = getSetOfRandomValues()
        val values = valuesSet.toList()
        val entries = values.flatMap { value -> List(3) { index -> "$value #$index" to value } }.sortedBy { it.first }
        val bst: BalancedSearchTreeList<String, Int> = getBstList(entries)
        val encountered = mutableSetOf<Int>()
        entries.forEachIndexed { index, (_, value) ->
            if (value !in encountered) {
                encountered.add(value)
                assertEquals(index, bst.indexOf(value))
            } else {
                assertNotEquals(index, bst.indexOf(value))
            }
        }

        getSetOfRandomValues().filter { it !in values }.forEach {
            assertEquals(-1, bst.indexOf(it))
        }
    }
}
