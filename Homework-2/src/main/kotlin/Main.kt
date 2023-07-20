import org.jub.kotlin.hometask3.getAvlTree
import kotlin.random.Random

fun main() {
    val wow = List(10) { it to Random.nextInt(100) }
    val avl = getAvlTree(wow)
    println(avl.maximumKey())
}
