package team

import models.*

internal val generateTeamsForOneRoundMethod = TestMethod(
    name = "generateTeamsForOneRound",
    returnType = KotlinType("List", params = listOf(teamClass.getFullName())),
    returnTypeJava = "List",
    arguments = listOf(
        Variable(
            name = "teamsNumber",
            javaType = "int",
        )
    )
)

internal val getTeamsStorageMethod = TestMethod(
    name = "access\$getTeamsStorage\$cp",
    returnType = KotlinType("Map"),
    returnTypeJava = "Map",
)

internal val teamServiceTestClass = TestClass(
    "TeamService",
    "jetbrains.kotlin.course.alias.team",
    declaredFields = listOf(
        Variable(
            name = "identifierFactory",
            javaType = "IdentifierFactory",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
        ),
        Variable(
            name = "teamsStorage",
            javaType = "Map",
            // Because it is inside companion object
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
            isStatic = true,
        ),
    ),
    customMethods = listOf(generateTeamsForOneRoundMethod),
)

internal val teamServiceCompanionTestClass = TestClass(
    "TeamService\$Companion",
    "jetbrains.kotlin.course.alias.team",
)
