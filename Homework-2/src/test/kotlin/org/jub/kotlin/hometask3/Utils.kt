package org.jub.kotlin.hometask3

import kotlin.random.Random

internal const val TEST_ITERATIONS = 10
internal const val TEST_SET_SIZE = 128

internal fun getSetOfRandomValues(size: Int = TEST_SET_SIZE) = IntArray(size) { Random.nextInt() }.toSet()

val Collection<Int>.withDoubles
    get() = zip(this.map { it.toDouble() })

val Collection<Int>.withStrings
    get() = zip(this.map { it.toString() })

internal sealed class Base(val param: Int) : Comparable<Base>
internal class Child1(p: Int) : Base(p) {
    override fun compareTo(other: Base): Int = if (other is Child1) param - other.param else -1
}
internal class Child2(p: Int) : Base(p) {
    override fun compareTo(other: Base): Int = if (other is Child2) other.param - param else 1
}
internal infix fun <T> Child1.withValue(value: T) = this to value
internal infix fun <T> Child2.withValue(value: T) = this to value
