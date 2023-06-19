package result

import models.*

internal val getAllGameResultsMethod = TestMethod(
    name = "getAllGameResults",
    returnType = KotlinType(
        "List",
        abbreviation = "kotlin.collections.List<jetbrains.kotlin.course.alias.results.GameResult>"
    ),
    returnTypeJava = "List",
)

internal val saveGameResultsMethod = TestMethod(
    name = "saveGameResults",
    returnType = KotlinType("Unit"),
    returnTypeJava = "void",
    arguments = listOf(
        Variable(
            name = "result",
            javaType = "List",
            kotlinType = KotlinType(
                "List",
                params = listOf("jetbrains.kotlin.course.alias.results.GameResult")
            ),
        ),
    ),
)

internal val gameHistoryVariable = Variable(
    name = "gameHistory",
    javaType = "List",
    kotlinType = KotlinType(
        "List",
        params = listOf("jetbrains.kotlin.course.alias.results.GameResult")
    ),
    // Because it is inside companion object
    visibility = Visibility.PRIVATE,
    mutability = VariableMutability.VAL,
    isStatic = true,
)

internal val gameResultsServiceTestClass = TestClass(
    "GameResultsService",
    "jetbrains.kotlin.course.alias.results",
    declaredFields = listOf(
        gameHistoryVariable,
    ),
    customMethods = listOf(
        getAllGameResultsMethod,
        saveGameResultsMethod,
    ),
)

internal val gameResultsServiceCompanionTestClass = TestClass(
    "GameResultsService\$Companion",
    "jetbrains.kotlin.course.alias.results",
)
