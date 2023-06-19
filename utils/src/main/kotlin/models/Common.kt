package models

import java.lang.reflect.Modifier
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

enum class Visibility(val key: String) {
    PUBLIC("public"),
    PRIVATE("private"),
    ;
}

private fun KType.checkNullability(kotlinType: KotlinType, errorMessagePrefix: String) {
    val nullablePrefix = if (!kotlinType.isNullable) "" else "not"
    assert(this.isMarkedNullable == kotlinType.isNullable) { "Error, $errorMessagePrefix must be $nullablePrefix nullable" }
}

private fun KType.getAbbreviation(): String? {
    val separator = " /*"
    val strRepresentation = this.toString()
    if (separator !in strRepresentation) {
        // Because we call it only if the abbreviation was defined into the expected type
        if (arguments.isNotEmpty()) {
            this.arguments.first().type?.toString()?.let {
                return it
            }
        }
        return null
    }
    val abr = strRepresentation.split(separator).first()
    return if ("<" in strRepresentation) {
        "$abr>"
    } else {
        abr
    }
}

@Suppress("ForbiddenComment")
private fun KType.checkAbbreviation(abbreviation: String, errorMessagePrefix: String) {
    val abr = this.getAbbreviation()
    // TODO: can we do it better?
    if (abr == null && this.toString() == abbreviation) {
        return
    }
    assert(abr != null) { "You need to create a value class or a type alias $abbreviation and use it as the return type for $errorMessagePrefix" }
    assert(abr!! == abbreviation) { "The return type for $errorMessagePrefix must contain $abbreviation" }
}

fun KType.checkType(
    kotlinType: KotlinType?,
    javaType: String,
    errorMessagePrefix: String,
    toCheckJavaType: Boolean = true
) {
    kotlinType?.let {
        this.checkNullability(kotlinType, errorMessagePrefix)
        it.abbreviation?.let { abr ->
            this.checkAbbreviation(abr, errorMessagePrefix)
        }
    }
    if (toCheckJavaType) {
        val message = "The return type of $errorMessagePrefix must be $javaType"
        // We have a parametrized type
        if ("<" in this.javaType.toString() && kotlinType?.abbreviation == null) {
            val type = kotlinType?.getTypePrettyString() ?: javaType
            assert(type.lowercase() in this.javaType.toString().lowercase()) { message }
        } else {
            assert(this.javaType.getShortName() == javaType.lowercase()) { message }
        }
    }
}

fun Type.getShortName() = if ("<" in this.toString()) {
    val types = this.toString().split("<").toMutableList()
    types[0].getShortName()
} else {
    this.toString().lowercase().split(".").last()
}

fun String.getShortName() = lowercase().split(".").last()

fun Class<*>.getDeclaredFieldsWithoutCompanion() =
    this.declaredFields.filter { it.name !in listOf("Companion", "INSTANCE") }

fun Int.getVisibility(): Visibility? {
    if (Modifier.isPublic(this)) {
        return Visibility.PUBLIC
    }
    if (Modifier.isPrivate(this)) {
        return Visibility.PRIVATE
    }
    return null
}
