package models

import java.lang.reflect.Method
import kotlin.reflect.jvm.kotlinFunction

data class TestMethod(
    val name: String,
    val returnType: KotlinType,
    val arguments: List<Variable> = emptyList(),
    val returnTypeJava: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
    val hasGeneratedPartInName: Boolean = false,
) {
    fun prettyString(withToDo: Boolean = true): String {
        val args = arguments.joinToString(", ") { it.paramPrettyString() }
        val body = if (withToDo) {
            "TODO(\"Not implemented yet\")"
        } else {
            "// Some code"
        }
        return "${visibility.key} fun $name($args): ${returnType.getTypePrettyString()} = $body"
    }

    private fun Variable.paramPrettyString() = "$name: $javaType"

    fun checkMethod(method: Method) {
        val kotlinFunction =
            method.kotlinFunction ?: error("Can not find Kotlin method for the method ${this.prettyString()}")
        assert(kotlinFunction.name == name) { "The function name must be: $name" }
        val visibility = kotlinFunction.visibility?.name?.lowercase()
        assert(visibility == this.visibility.key) { "The visibility of the method $name must be ${this.visibility.key}" }
        kotlinFunction.returnType.checkType(returnType, returnTypeJava ?: returnType.type, "the function $name")
    }
}

private fun List<Method>.filterByCondition(errorMessage: String, condition: (Method) -> Boolean): List<Method> {
    val filteredByCondition = this.filter { condition(it) }
    if (filteredByCondition.isEmpty()) {
        assert(false) { errorMessage }
    }
    return filteredByCondition
}

fun Array<Method>.findMethod(method: TestMethod): Method {
    val filteredByName =
        this.toList().filterByCondition("The method ${method.prettyString()} is missed") {
            if (method.hasGeneratedPartInName) {
                method.name in it.name
            } else {
                it.name == method.name
            }
        }
    val returnTypeJava = method.returnTypeJava ?: method.returnType.type
    val filteredByType =
        filteredByName.filterByCondition("The method ${method.name} should have the return type ${method.returnType.getTypePrettyString()}") {
            it.returnType.name.getShortName().lowercase() == returnTypeJava.lowercase()
        }
    val filteredByArgumentsCount =
        filteredByType.filterByCondition("The method ${method.name} should have ${method.arguments.size} arguments") { it.parameterCount == method.arguments.size }
    require(filteredByArgumentsCount.size == 1) { "The method ${method.prettyString()} is missed" }
    val m = filteredByArgumentsCount.first()
    val params = m.parameterTypes.map { it.name.getShortName().lowercase() }
    val args = method.arguments.map { it.javaType.lowercase() }
    assert(params == args) { "The method ${method.name} should have ${method.arguments.size} arguments: $params. The full signature is: ${method.prettyString()}." }
    return m
}

fun Method.invokeWithoutArgs(
    clazz: Class<*>,
    isPrivate: Boolean = false,
    obj: Any? = null,
): Any = invokeWithArgs(clazz = clazz, isPrivate = isPrivate, obj = obj)

fun Method.invokeWithArgs(
    vararg args: Any,
    clazz: Class<*>,
    isPrivate: Boolean = false,
    obj: Any? = null,
): Any {
    if (isPrivate) {
        this.isAccessible = true
    }
    return obj?.let { invoke(it, *args) } ?: invoke(clazz, *args)
}

@Suppress("SpreadOperator")
data class TestMethodInvokeData(
    val testClass: TestClass,
    val testMethod: TestMethod,
    val constructorArgumentsTypes: List<Class<*>> = emptyList(),
    val constructorArguments: List<Any> = emptyList(),
) {
    val clazz = testClass.getJavaClass()
    val method = if (testMethod.visibility == Visibility.PRIVATE) {
        clazz.declaredMethods.findMethod(testMethod)
    } else {
        clazz.methods.findMethod(testMethod)
    }
    val instance: Any = clazz.getConstructor(*constructorArgumentsTypes.toTypedArray())
        .newInstance(*constructorArguments.toTypedArray())
}
