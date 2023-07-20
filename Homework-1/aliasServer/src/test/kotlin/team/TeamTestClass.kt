package team

import utils.kotlinTypeIdentifier
import models.*

internal val getNameFromTeamMethod = TestMethod(
    name = "getName",
    returnType = KotlinType("String"),
    returnTypeJava = "string",
)

internal val teamClass = TestClass(
    "Team",
    "jetbrains.kotlin.course.alias.team",
    isDataClass = true,
    declaredFields = listOf(
        Variable(
            name = "id",
            javaType = "int",
            kotlinType = kotlinTypeIdentifier,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = true,
        ),
        Variable(
            name = "points",
            javaType = "int",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = true,
        ),
        Variable(
            name = "name",
            javaType = "string",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL,
        )
    ),
)
