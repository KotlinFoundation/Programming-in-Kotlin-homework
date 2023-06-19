import card.*
import commonTests.team.checkNameAndIdFieldsValue
import jetbrains.kotlin.course.alias.util.words
import models.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import result.*
import team.*
import utils.*
import java.lang.reflect.InvocationTargetException

class Test {
    companion object {

        private val emptyList = emptyList<String>()
        private val listWithOneWord = listOf("dog")
        private val listWithSeveralWords = listOf("dog", "cat")

        private val expectedCardsListSize = words.size / 4
        private const val WORD = "Word"

        @JvmStatic
        fun toWordsMethodTestData() = listOf(
            // initial, expected
            Arguments.of(emptyList, emptyList.toListWithWordsString()),
            Arguments.of(listWithOneWord, listWithOneWord.toListWithWordsString()),
            Arguments.of(listWithSeveralWords, listWithSeveralWords.toListWithWordsString()),
        )

        private fun List<String>.toListWithWordsString() = "[${this.joinToString(", ") { "$WORD(word=$it)" }}]"

        private fun String.wordMatches() = Regex(WORD).findAll(this).toList().size

        private fun callGetCardByIndexMethod(invokeData: TestMethodInvokeData, index: Int) =
            cardServiceTestClass.invokeMethodWithArgs(
                args = arrayOf(index),
                invokeData = invokeData,
            ).toString()
    }
    @Test
    fun gameResultsServiceTest() {
        val clazz = gameResultsServiceTestClass.checkBaseDefinition()
        gameResultsServiceCompanionTestClass.checkBaseDefinition()
        gameResultsServiceTestClass.checkFieldsDefinition(clazz, false)
        gameResultsServiceTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
            )
        )
        gameResultsServiceTestClass.checkDeclaredMethods(clazz)
    }

    @Test
    fun saveGameResultsMethodTest() {
        commonTests.results.saveGameResultsMethodTest(
            teamServiceTestClass,
            generateTeamsForOneRoundMethod,
            gameResultsServiceTestClass,
            saveGameResultsMethod,
            gameHistoryVariable,
            teamClassTestClass = teamClass
        )
    }

    @Test
    fun getAllGameResultsMethodTest() {
        commonTests.results.getAllGameResultsMethodTest(
            gameResultsServiceTestClass,
            getAllGameResultsMethod,
            gameHistoryVariable,
            teamServiceTestClass,
            generateTeamsForOneRoundMethod,
            saveGameResultsMethod,
        )
    }

    @Test
    fun cardServiceTest() {
        val clazz = cardServiceTestClass.checkBaseDefinition()
        cardServiceCompanionTestClass.checkBaseDefinition()

        cardServiceTestClass.checkFieldsDefinition(clazz, false)
        val identifierFactoryClazz = identifierFactoryClass.getJavaClass()
        cardServiceTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(identifierFactoryClazz)),
            )
        )

        // Check WORDS_IN_CARD and cardsAmount values
        val instance = clazz.getConstructor().newInstance()
        val getCardsAmountJavaMethod = clazz.methods.findMethod(getCardsAmountMethod)
        val field = clazz.declaredFields.find { it.name == wordsInCardTestVariable.name }
            ?: error("Can not find the field ${wordsInCardTestVariable.name}")
        field.isAccessible = true
        val wordsInCardVariable = field.get(instance)
        assert(4 == wordsInCardVariable as Int) { "The value of the field ${wordsInCardTestVariable.name} must be 4." }
        val cardsAmount = identifierFactoryClass.invokeMethodWithoutArgs(clazz, instance, getCardsAmountJavaMethod)
        val expectedCardsAmount = words.size / wordsInCardVariable
        assert(expectedCardsAmount == cardsAmount as Int) { "The value in the field cardsAmount must be calculated as: words.size / WORDS_IN_CARD" }

        cardServiceTestClass.checkDeclaredMethods(clazz)
    }

    @ParameterizedTest
    @MethodSource("toWordsMethodTestData")
    fun toWordsMethodTest(words: List<String>, expected: String) {
        val invokeData = TestMethodInvokeData(cardServiceTestClass, toWordsMethod)
        val actualWords = cardServiceTestClass.invokeMethodWithArgs(
            args = arrayOf(words),
            invokeData = invokeData,
            isPrivate = true,
        ).toString()
        assert(expected == actualWords) { "For the list $words the method ${toWordsMethod.name} must return $expected." }
    }

    @Test
    fun generateCardsMethodTest() {
        val invokeData = TestMethodInvokeData(cardServiceTestClass, generateCardsMethod)
        (cardServiceTestClass.invokeMethodWithoutArgs(
            invokeData,
            isPrivate = true,
        ) as? ArrayList<*>)?.let { cards ->
            assert(expectedCardsListSize == cards.size) { "The method $generateCardsMethod must return a list of Cards with $expectedCardsListSize items." }
            cards.forEachIndexed { id, item ->
                val strItem = item.toString()
                // This method will be called during the class initialization, so we need to shift all ids
                assert((id + expectedCardsListSize).toString() in strItem) { "Each card must have an unique identifier." }
                assert(4 == strItem.wordMatches()) { "Each card must have only four words." }
            }
        } ?: assert(false) { "The method ${generateCardsMethod.prettyString()} must return a list of Cards" }
    }

    @Test
    fun getCardByIndexMethodTest() {
        val invokeData = TestMethodInvokeData(cardServiceTestClass, getCardByIndexMethod)
        assertThrows<InvocationTargetException>("\"The method ${getCardByIndexMethod.name} must throw an exception for index = -1.") {
            callGetCardByIndexMethod(invokeData, -1)
        }
        for (i in 0 until expectedCardsListSize) {
            try {
                val card = callGetCardByIndexMethod(invokeData, i)
                assert("id=$i" in card) { "The generated card at the position $i in the cards list must have id = $i." }
                assert(4 == card.wordMatches()) { "Each card must have only four words." }
            } catch (e: InvocationTargetException) {
                assert(false) { "The method ${getCardByIndexMethod.name} must not throw any exceptions for index = $i." }
            }
        }
    }
    @Test
    fun cardTest() {
        val clazz = cardTestClass.checkBaseDefinition()
        cardTestClass.checkFieldsDefinition(clazz)
        val constructor = cardTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(
                    parameterTypes = listOf(Int::class.java, List::class.java),
                )
            )
        )
        // Just check if the constructor works well
        // Unfortunately we can not check the type of the argument, because we don't have them at runtime
        constructor.newInstance(1, listOf("dog"))
    }

    @Test
    fun teamTeamServiceTest() {
        val clazz = teamServiceTestClass.checkBaseDefinition()
        teamServiceCompanionTestClass.checkBaseDefinition()
        teamServiceTestClass.checkFieldsDefinition(clazz, false)
        val identifierFactoryClazz = identifierFactoryClass.getJavaClass()
        teamServiceTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(identifierFactoryClazz)),
            )
        )
        teamServiceTestClass.checkDeclaredMethods(clazz)
    }

    @Test
    fun generateTeamsForOneRoundMethodTest() {
        commonTests.team.generateTeamsForOneRoundMethodTest(
            teamServiceTestClass,
            generateTeamsForOneRoundMethod,
            getTeamsStorageMethod
        )
    }

    @Test
    fun teamClassTest() {
        val clazz = teamClass.checkBaseDefinition()
        teamClass.checkFieldsDefinition(clazz)
        val constructor = teamClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(
                    parameterTypes = listOf(Int::class.java),
                    defaultParameterTypes = listOf(Int::class.java),
                )
            )
        )
        // Check the name field value
        checkNameAndIdFieldsValue(
            clazz,
            teamClass,
            getNameFromTeamMethod,
        ) { i: Int -> constructor.newInstance(i, 0, 0, null) }
    }
    @Test
    fun identifierFactoryClassTest() {
        val clazz = identifierFactoryClass.checkBaseDefinition()
        identifierFactoryClass.checkFieldsDefinition(clazz)
        identifierFactoryClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(Int::class.java)),
            )
        )
        identifierFactoryClass.checkDeclaredMethods(clazz)
    }

    @Test
    fun uniqueIdentifierMethodTest() {
        val invokeData = TestMethodInvokeData(identifierFactoryClass, uniqueIdentifierMethod)
        for (i in 0..100) {
            val id =
                identifierFactoryClass.invokeMethodWithoutArgs(invokeData.clazz, invokeData.instance, invokeData.method)
            assert(id == i) { "The ${uniqueIdentifierMethod.name} works incorrect. Try to get id $i-th time, it should be $i, but was $id" }
        }
    }
}
