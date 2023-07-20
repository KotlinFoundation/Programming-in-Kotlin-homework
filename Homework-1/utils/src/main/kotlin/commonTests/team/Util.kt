@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

package commonTests.team

import models.*

fun generateTeamsStringRepresentation(startId: Int, n: Int, toAddId: Boolean = false): List<String> {
    var id = startId
    val teams = mutableListOf<String>()
    repeat(n) {
        val prefix = if (toAddId) {
            "$id="
        } else {
            ""
        }
        teams.add("${prefix}Team(id=$id, points=0)")
        id++
    }
    return teams
}

fun teamsOutput(startId: Int, n: Int) =
    "[${generateTeamsStringRepresentation(startId, n).joinToString(", ")}]"


fun TestClass.callGenerateTeamsForOneRound(invokeData: TestMethodInvokeData, n: Int) =
    this.invokeMethodWithArgs(
        n,
        clazz = invokeData.clazz,
        instance = invokeData.instance,
        javaMethod = invokeData.method,
    )

fun <T> checkNameAndIdFieldsValue(
    teamsClazz: Class<*>,
    teamsClass: TestClass,
    getNameFromTeamMethod: TestMethod,
    getIdFromTeamMethod: TestMethod? = null,
    newInstance: (i: Int) -> T,
) {
    val getNameMethod = teamsClazz.methods.findMethod(getNameFromTeamMethod)
    val getIdMethod = getIdFromTeamMethod?.let { teamsClazz.methods.findMethod(getIdFromTeamMethod) }
    for (i in 0..100) {
        val instance = try {
            newInstance(i)
        } catch (e: Exception) {
            assert(false) { "Can not create an instance of the class ${teamsClass.getFullName()} with constructor without arguments" }
        }
        getIdMethod?.let {
            val id = teamsClass.invokeMethodWithoutArgs(teamsClazz, instance, getIdMethod)
            assert(id == i) { "For the i-th team id must be $i" }
        }
        val name = teamsClass.invokeMethodWithoutArgs(teamsClazz, instance, getNameMethod)
        val teamName = "Team#${i + 1}"
        assert(teamName == name) { "For the team with id $i the name must be $teamName" }
    }
}

fun generateTeamsForOneRoundMethodTest(
    teamServiceTestClass: TestClass,
    generateTeamsForOneRoundMethod: TestMethod,
    getTeamsStorageMethod: TestMethod,
) {
    val invokeData = TestMethodInvokeData(teamServiceTestClass, generateTeamsForOneRoundMethod)
    val n = 5
    val teamsStorageMethod = invokeData.clazz.methods.findMethod(getTeamsStorageMethod)
    val teamsStorageSb = StringBuilder()
    repeat(n) {
        val teams = teamServiceTestClass.callGenerateTeamsForOneRound(invokeData, n)
        val expected = teamsOutput(it * n, n)
        assert(expected == teams.toString()) { "${generateTeamsForOneRoundMethod.name} must return $expected for teamsNumber = $n and $it-th attempt." }
        if (it > 0) {
            teamsStorageSb.append(", ")
        }
        teamsStorageSb.append(generateTeamsStringRepresentation(it * n, n, true).joinToString(", "))
        val teamsStorageRes = teamServiceTestClass.invokeMethodWithoutArgs(
            clazz = invokeData.clazz,
            instance = invokeData.instance,
            javaMethod = teamsStorageMethod
        ).toString()
        val expectedTeamsStorage = "{$teamsStorageSb}"
        assert(expectedTeamsStorage == teamsStorageRes) { "You need to save generated teams into the teamsStorage after each generation." }
    }
}

fun resetIdCounter(teamsClazz: Class<*>, idCounterVariable: Variable) {
    val idCounterField = teamsClazz.declaredFields.find { it.name == idCounterVariable.name }
        ?: error("Can not find the field ${idCounterVariable.name}")
    idCounterField.isAccessible = true
    idCounterField.set(teamsClazz, 0)
}
