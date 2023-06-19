package commonTests.results

import commonTests.team.callGenerateTeamsForOneRound
import models.*
import java.lang.reflect.InvocationTargetException
import org.junit.jupiter.api.assertThrows

@Suppress("SpreadOperator", "ForbiddenComment")
fun callSaveGameResultsMethod(teams: Any, invokeData: TestMethodInvokeData, gameHistoryVariable: Variable): String {
    val field = invokeData.clazz.declaredFields.find { it.name == gameHistoryVariable.name }
        ?: error("Can not find the field ${gameHistoryVariable.name}")
    field.isAccessible = true
    // TODO: why gameResultsServiceTestClass.invokeMethodWithArgs does not work?
    invokeData.method.invoke(invokeData.instance, *arrayOf(teams))
    return field.get(invokeData.instance).toString()
}

@Suppress("LongParameterList")
fun saveGameResultsMethodTest(
    teamServiceTestClass: TestClass,
    generateTeamsForOneRoundMethod: TestMethod,
    gameResultsServiceTestClass: TestClass,
    saveGameResultsMethod: TestMethod,
    gameHistoryVariable: Variable,
    teamClassTestClass: TestClass? = null,
) {
    val gameResultsInvokeData = TestMethodInvokeData(gameResultsServiceTestClass, saveGameResultsMethod)

    // Check if the method throws an error with empty list with teams
    assertThrows<InvocationTargetException>("The ${saveGameResultsMethod.name} must throw an error if the list with teams is empty") {
        callSaveGameResultsMethod(emptyList<Any>(), gameResultsInvokeData, gameHistoryVariable)
    }

    // Check if the method throws an error with an unknown team
    teamClassTestClass?.let {
        val teamClazz = teamClassTestClass.findClass()
        val constructor = teamClassTestClass.checkConstructors(
            teamClazz,
            listOf(
                ConstructorGetter(
                    parameterTypes = listOf(Int::class.java),
                    defaultParameterTypes = listOf(Int::class.java),
                )
            )
        )
        val team = constructor.newInstance(-1, 0, 0, null)
        assertThrows<InvocationTargetException>("The ${saveGameResultsMethod.name} must throw an error if you try to add a team which was not added into teamsStorage") {
            callSaveGameResultsMethod(
                listOf(team),
                gameResultsInvokeData,
                gameHistoryVariable,
            )
        }
    }

    val teamInvokeData = TestMethodInvokeData(teamServiceTestClass, generateTeamsForOneRoundMethod)
    val teams = teamServiceTestClass.callGenerateTeamsForOneRound(teamInvokeData, 4)
    assert(
        "$teams" in callSaveGameResultsMethod(
            teams,
            gameResultsInvokeData,
            gameHistoryVariable,
        )
    ) { "Try to save the game results for the teams: $teams, but they are not in the ${gameHistoryVariable.name} field." }
}

@Suppress("LongParameterList", "MaxLineLength")
fun getAllGameResultsMethodTest(
    gameResultsServiceTestClass: TestClass,
    getAllGameResultsMethod: TestMethod,
    gameHistoryVariable: Variable,
    teamServiceTestClass: TestClass,
    generateTeamsForOneRoundMethod: TestMethod,
    saveGameResultsMethod: TestMethod,
) {
    val invokeData = TestMethodInvokeData(gameResultsServiceTestClass, getAllGameResultsMethod)
    val field = invokeData.clazz.declaredFields.find { it.name == gameHistoryVariable.name }
        ?: error("Can not find the field ${gameHistoryVariable.name}")
    field.isAccessible = true
    val emptyResults = field.get(invokeData.instance) as ArrayList<*>
    assert(emptyResults.size == 0) { "After initialization of the class ${gameResultsServiceTestClass.name} the field ${gameHistoryVariable.name} must store an empty list." }
    val reversedEmptyResults = gameResultsServiceTestClass.invokeMethodWithoutArgs(invokeData)
    assert("${emptyResults.reversed()}" == "$reversedEmptyResults") { "Try to call the ${getAllGameResultsMethod.name} on an empty list, but got $reversedEmptyResults" }

    val teamInvokeData = TestMethodInvokeData(teamServiceTestClass, generateTeamsForOneRoundMethod)
    val teams = teamServiceTestClass.callGenerateTeamsForOneRound(teamInvokeData, 3)
    val gameResultsInvokeData = TestMethodInvokeData(gameResultsServiceTestClass, saveGameResultsMethod)
    callSaveGameResultsMethod(teams, gameResultsInvokeData, gameHistoryVariable)
    val notEmptyResults = field.get(invokeData.instance) as ArrayList<*>
    val reversedNotEmptyResults = gameResultsServiceTestClass.invokeMethodWithoutArgs(invokeData)
    assert("${notEmptyResults.reversed()}" == "$reversedNotEmptyResults") { "Try to call the ${getAllGameResultsMethod.name} on the $notEmptyResults list, got $reversedNotEmptyResults" }
}
