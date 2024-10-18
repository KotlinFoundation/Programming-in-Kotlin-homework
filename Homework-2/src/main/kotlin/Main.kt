import org.jub.kotlin.hometask3.getBst
import kotlin.random.Random

fun main() {
    val inputData = List(10) { it to Random.nextInt(100) }
    val bst = getBst(inputData)
    val maxKeyBst = bst.maximumKey()
    val maxKeyList = inputData.maxOf { it.first }
    check(maxKeyBst == maxKeyList) {
        "Something is wrong: $maxKeyBst VS $maxKeyList"
    }
}
