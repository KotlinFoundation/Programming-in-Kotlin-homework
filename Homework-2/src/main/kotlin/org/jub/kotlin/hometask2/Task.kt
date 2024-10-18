package org.jub.kotlin.hometask3

interface BalancedSearchTree<K : Comparable<K>, V> {
    /* abstract */ val height: Int

    /**
     * All maximum/minimum functions should throw some Exception if the tree is empty
     */
    fun maximumKey(): K

    fun minimumKey(): K

    fun maximumValue(): V

    fun minimumValue(): V
}

/**
 * We would strongly advise to implement Mutable Version right from the start. This way constructor from a Collection of
 * Pairs can simply call put for each pair. Otherwise, you should write a constructor yourself.
 */
interface BalancedSearchTreeMap<K : Comparable<K>, V> : Map<K, V>, BalancedSearchTree<K, V>

interface MutableBalancedSearchTreeMap<K : Comparable<K>, V> : MutableMap<K, V>, BalancedSearchTreeMap<K, V> {
    /**
     * Type parameters of `other` here and in `mergeWith` below are wrong. Fix, please
     * This method has a requirement that keys of `other` should be larger than keys of `this`, otherwise an Exception
     * should be thrown.
     */
    fun merge(other: MutableBalancedSearchTreeMap<K, V>): MutableBalancedSearchTreeMap<K, V>
}

infix fun <K : Comparable<K>, V> MutableBalancedSearchTreeMap<K, V>.mergeWith(other: MutableBalancedSearchTreeMap<K, V>) = merge(other)

/**
 * AbstractIterator might help you implement Iterator.
 * An easy way to implement would be to store index and call List::get(index), but we don't like easy ways.
 */
interface BalancedSearchTreeList<K : Comparable<K>, V> : List<V>, BalancedSearchTree<K, V>
