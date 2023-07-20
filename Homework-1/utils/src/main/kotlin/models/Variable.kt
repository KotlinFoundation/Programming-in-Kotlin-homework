package models

import throwInternalCourseError
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinProperty

enum class VariableMutability(val key: String) {
    VAL("val"),
    VAR("var"),
    JAVA_MUTABILITY(""),
    ;
}

fun VariableMutability?.compareWith(expected: VariableMutability?): Boolean {
    if (this == VariableMutability.JAVA_MUTABILITY) {
        // We can not define the mutability :(
        return true
    }
    return this == expected
}

data class KotlinType(
    val type: String,
    val abbreviation: String? = null,
    val isNullable: Boolean = false,
    val params: List<String> = emptyList(),
) {
    fun getTypePrettyString() = abbreviation ?: run {
        if (params.isNotEmpty()) {
            "$type<${params.joinToString(", ")}>"
        } else {
            type
        }
    }
}

private data class FieldProperties(
    val name: String,
    val visibilityKey: String?,
    val mutability: VariableMutability?,
    val javaType: String,
) {
    companion object {
        fun buildByKotlinProp(kotlinProp: KProperty<*>) = FieldProperties(
            kotlinProp.name,
            kotlinProp.visibility?.name,
            kotlinProp.getVariableMutability(),
            kotlinProp.returnType.javaType.getShortName(),
        )

        fun buildByJavaField(field: Field) = FieldProperties(
            field.name,
            field.getVisibility()?.name,
            VariableMutability.JAVA_MUTABILITY,
            field.type.getShortName(),
        )
    }

    fun checkProperties(variable: Variable) {
        assert(name == variable.name) { "The field name must be: ${variable.name}" }
        val visibilityErrorMessage = variable.visibility?.let {
            "The visibility of the field ${variable.name} must be ${it.key}"
        } ?: "The filed ${variable.name} should not have any modifiers"
        assert(visibilityKey?.lowercase() == variable.visibility?.key) { visibilityErrorMessage }
        val mutabilityErrorMessage = variable.mutability?.let {
            "The field ${variable.name} must be ${it.key}"
        } ?: "The filed ${variable.name} should not have val or var key words"
        assert(mutability.compareWith(variable.mutability)) { mutabilityErrorMessage }
        assert(javaType == variable.javaType.lowercase()) { "The return type of the field ${variable.name} must be $javaType" }
    }
}

data class Variable(
    val name: String,
    val javaType: String,
    val value: String? = null,
    val kotlinType: KotlinType? = null,
    val visibility: Visibility? = null,
    val mutability: VariableMutability? = null,
    val isInPrimaryConstructor: Boolean = false,
    val isStatic: Boolean = false,
    val isConst: Boolean = false,
) {
    private fun getTypePrettyString() = kotlinType?.getTypePrettyString() ?: javaType

    fun prettyString(): String {
        val currentMutability = mutability?.key ?: ""
        val prefix = visibility?.let {
            "${visibility.key} $currentMutability "
        } ?: ""
        return "$prefix$name: ${getTypePrettyString()}"
    }

    fun checkField(field: Field) {
        val commonProp =
            field.kotlinProperty?.let { FieldProperties.buildByKotlinProp(it) } ?: FieldProperties.buildByJavaField(
                field
            )
        commonProp.checkProperties(this)
        if (isStatic) {
            assert(Modifier.isStatic(field.modifiers)) { "The field $name must be defined into an object or a companion object." }
        }
        if (isConst) {
            val errorMessage = "The field $name must be a const value."
            assert(Modifier.isFinal(field.modifiers)) { errorMessage }
            field.kotlinProperty?.isConst?.let {
                assert(it) { errorMessage }
            }
        }
        field.kotlinProperty?.returnType?.checkType(
            kotlinType,
            javaType,
            "the field $name",
            false
        )
    }

}

private fun Field.getVisibility() = this.modifiers.getVisibility()

fun KProperty<*>.getVariableMutability(): VariableMutability? {
    val strRepresentation = this.toString()
    if (VariableMutability.VAL.key in strRepresentation) {
        return VariableMutability.VAL
    }
    if (VariableMutability.VAR.key in strRepresentation) {
        return VariableMutability.VAR
    }
    return null
}

fun Variable.variableDefTemplateBase() = "val ${this.name} = ${this.value}"

fun Variable.variableDefTemplateWithType() = "val ${this.name}: ${this.javaType} = ${this.value}"

fun Variable.isVariableExist(fileContent: String): Boolean {
    val baseDef = variableDefTemplateBase()
    val defWithType = variableDefTemplateWithType()
    if (!(baseDef in fileContent || defWithType in fileContent)) {
        error(
            "The code should contains a definition of the ${this.name} variable! " +
                    "Please, add <$baseDef> or <$defWithType> code in your solution."
        )
    }
    return true
}

@Suppress("ForbiddenComment")
fun checkListOfVariables(sourceCodeFile: File, variables: List<Variable>) {
    if (sourceCodeFile.exists()) {
        val content = sourceCodeFile.readText()
        for (variable in variables) {
            assert(variable.isVariableExist(content))
        }
    } else {
        // TODO: log some errors?
        throwInternalCourseError()
    }
}
