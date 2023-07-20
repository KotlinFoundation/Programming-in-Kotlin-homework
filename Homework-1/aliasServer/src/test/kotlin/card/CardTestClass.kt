package card

import utils.kotlinTypeIdentifier
import models.*

internal val cardTestClass = TestClass(
    "Card",
    "jetbrains.kotlin.course.alias.card",
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
            name = "words",
            javaType = "List",
            kotlinType = KotlinType(
                "List",
                abbreviation = "jetbrains.kotlin.course.alias.card.Word",
                params = listOf("String")
            ),
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = true,
        ),
    )
)
