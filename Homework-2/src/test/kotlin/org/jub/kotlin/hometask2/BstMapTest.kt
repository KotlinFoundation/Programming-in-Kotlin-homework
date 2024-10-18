package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import kotlin.test.*

internal class BstMapTest {
    @Test
    fun getSizeEmpty() {
        val emptyBst: BalancedSearchTreeMap<Int, String> = getBstMap(emptyList())
        assertEquals(0, emptyBst.size, "Default size should be 0")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun getSize() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeMap<Int, String> = getBstMap(values.withStrings)
        assertEquals(values.size, bst.size, "Values and bst size should be equal")
    }

    @Test
    fun isEmpty() {
        val emptybst: BalancedSearchTreeMap<Int, String> = getBstMap(emptyList())
        assertTrue(emptybst.isEmpty(), "Default tree should be empty")
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun getEntries() {
        val values = getSetOfRandomValues()
        val doubleValues = values.map { it.toDouble() }.toSet()
        val bst: BalancedSearchTreeMap<Int, Double> = getBstMap(values.zip(doubleValues))
        val entries = bst.entries
        assertTrue(entries.all { it.key in values && it.value in doubleValues })
        assertTrue(values.all { value -> value in entries.map { it.key } })
        assertTrue(doubleValues.all { doubleValue -> doubleValue in entries.map { it.value } })
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun containsKey() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeMap<Int, Double> = getBstMap(values.withDoubles)
        assertTrue(values.all { bst.containsKey(it) })
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(bst.containsKey(it))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun containsValue() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeMap<Int, Double> = getBstMap(values.withDoubles)
        assertTrue(values.all { bst.containsValue(it.toDouble()) })
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(bst.containsValue(it.toDouble()))
        }
    }

    @RepeatedTest(TEST_ITERATIONS)
    fun get() {
        val values = getSetOfRandomValues()
        val bst: BalancedSearchTreeMap<Int, Double> = getBstMap(values.withDoubles)
        values.forEach {
            assertEquals(it.toDouble(), bst[it])
        }
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertNull(bst[it])
        }
    }
}
