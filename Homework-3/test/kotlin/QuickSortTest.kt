import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class QuickSortTest {
    companion object {
        @JvmStatic
        fun numbers() = listOf(
            Arguments.of(
                emptyList<Int>(),
                emptyList<Int>(),
            ),
            Arguments.of(
                listOf(1),
                listOf(1),
            ),
            Arguments.of(
                listOf(1, 2, 1),
                listOf(1, 1, 2),
            ),
            Arguments.of(
                listOf(95, 68, 37, 4, 79, 94, 38, 27, 57, 14),
                listOf(4, 14, 27, 37, 38, 57, 68, 79, 94, 95),
            )
        )
    }

    @ParameterizedTest
    @MethodSource("numbers")
    fun quickSortImplTest(initialList: List<Int>, expectedList: List<Int>) {
        assertEquals(expectedList, quickSort(initialList))
    }
}
