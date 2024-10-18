import kotlin.random.Random
import utils.getStringRepresentation

fun <T : Comparable<T>> quickSort(items: List<T>): List<T> {
    if (items.size < 2) {
        return items
    }
    val pivot = items[items.size / 2]
//    val equal = items.filter { it == pivot }
    val less = items.filter { it < pivot }
    val greater = items.filter { it > pivot }
//    return quickSort(less) + equal + quickSort(greater)
    return quickSort(less) + quickSort(greater)
}

fun main() {
    println("Before quicksort:")
    val numbers  = List(10) { Random.nextInt(0, 100) }
    println(numbers.getStringRepresentation())
    println("After quicksort:")
    println(quickSort(numbers).getStringRepresentation())
}
