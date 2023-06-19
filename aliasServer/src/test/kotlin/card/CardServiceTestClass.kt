package card

import models.*

internal val getCardByIndexMethod = TestMethod(
    name = "getCardByIndex",
    returnType = KotlinType("Card"),
    returnTypeJava = "Card",
    arguments = listOf(
        Variable(
            name = "index",
            javaType = "int",
        )
    )
)

internal val toWordsMethod = TestMethod(
    name = "toWords",
    returnType = KotlinType("List", params = listOf("jetbrains.kotlin.course.alias.card.Word")),
    returnTypeJava = "List",
    arguments = listOf(
        Variable(
            name = "this",
            javaType = "List",
        ),
    ),
    visibility = Visibility.PRIVATE,
)

internal val generateCardsMethod = TestMethod(
    name = "generateCards",
    returnType = KotlinType("List", params = listOf("jetbrains.kotlin.course.alias.card.Card")),
    returnTypeJava = "List",
    visibility = Visibility.PRIVATE,
)

internal val getCardsAmountMethod = TestMethod(
    name = "access\$getCardsAmount\$cp",
    returnType = KotlinType("Int"),
    returnTypeJava = "Int",
    visibility = Visibility.PRIVATE,
)

internal val wordsInCardTestVariable = Variable(
    name = "WORDS_IN_CARD",
    javaType = "Int",
    visibility = Visibility.PRIVATE,
    mutability = VariableMutability.VAL,
    isConst = true,
    isStatic = true,
)

internal val cardsVariable = Variable(
    name = "cards",
    javaType = "List",
    kotlinType = KotlinType(
        "List",
        abbreviation = "jetbrains.kotlin.course.alias.card.Card"
    ),
    visibility = Visibility.PRIVATE,
    mutability = VariableMutability.VAL,
)

internal val cardServiceTestClass = TestClass(
    "CardService",
    "jetbrains.kotlin.course.alias.card",
    declaredFields = listOf(
        Variable(
            name = "identifierFactory",
            javaType = "IdentifierFactory",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
        ),
        cardsVariable,
        wordsInCardTestVariable,
        Variable(
            name = "cardsAmount",
            javaType = "Int",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
            isStatic = true,
        ),
    ),
    customMethods = listOf(
        getCardByIndexMethod,
        toWordsMethod,
        generateCardsMethod,
    ),
)

internal val cardServiceCompanionTestClass = TestClass(
    "CardService\$Companion",
    "jetbrains.kotlin.course.alias.card",
)
