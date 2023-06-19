@file:Suppress("SwallowedException")

package models

import throwInternalCourseError
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.jvm.internal.DefaultConstructorMarker

enum class ClassType(val key: String) {
    CLASS("class"), INTERFACE("interface"), SAM_INTERFACE("fun interface"), ENUM("enum class"), OBJECT("object"),
//    COMPANION_OBJECT
    ;
}

data class ConstructorGetter(
    val parameterTypes: List<Class<*>> = emptyList(),
    val defaultParameterTypes: List<Class<*>> = emptyList(),
    val toAddDefaultConstructorMarker: Boolean = false
) {
    @Suppress("SpreadOperator")
    fun getConstructorWithDefaultArguments(clazz: Class<*>) = try {
        val parameters =
            (parameterTypes + defaultParameterTypes.map { listOf(it, Int::class.java) }.flatten()).toMutableList()
        if (defaultParameterTypes.isNotEmpty() || toAddDefaultConstructorMarker) {
            parameters.add(DefaultConstructorMarker::class.java)
        }
        clazz.getConstructor(*parameters.toTypedArray())
    } catch (e: NoSuchMethodException) {
        null
    }
}

@Suppress("TooManyFunctions")
data class TestClass(
    val name: String = "MainKt",
    val classPackage: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
    val classType: ClassType = ClassType.CLASS,
    val declaredFields: List<Variable> = emptyList(),
    val customMethods: List<TestMethod> = emptyList(),
    val isDataClass: Boolean = false,
    val declaredEnumEntries: List<Variable> = emptyList(),
    val interfaces: List<TestClass> = emptyList(),
) {
    fun getFullName() = classPackage?.let {
        "$it.$name"
    } ?: name

    private fun getBaseDefinition() = "${visibility.key} ${classType.key} $name"

    private fun getFieldsListPrettyString() = declaredFields.joinToString { it.prettyString() }

    fun checkBaseDefinition(): Class<*> {
        val clazz = this.findClassSafe()
        val errorMessage = "You need to add: ${this.getBaseDefinition()}"
        assert(clazz != null) { errorMessage }
        assert(
            clazz!!.isSameWith(this)
        ) {
            "$errorMessage, but currently you added: ${
                clazz.toTestClass(this.name, this.classPackage).getBaseDefinition()
            }"
        }
        if (isDataClass) {
            clazz.checkIfIsDataClass(this)
        }
        if (this.interfaces.isNotEmpty()) {
            checkInterfaces(clazz)
        }
        return clazz
    }

    private fun checkInterfaces(clazz: Class<*>) {
        val clazzInterfaces = clazz.interfaces
        assert(this.interfaces.size == clazzInterfaces.size) { "The class ${getFullName()} must have ${this.interfaces.size} direct superclasses" }
        this.interfaces.forEach {
            val currentClazz = it.findClass()
            assert(currentClazz in clazzInterfaces) { "The class ${getFullName()} must have ${it.getFullName()} as a direct superclass" }
        }
    }

    private fun checkFields(clazz: Class<*>) {
        checkVariables(clazz, this.declaredFields)
    }

    private fun checkVariables(clazz: Class<*>, variables: List<Variable>) {
        val declaredFields = clazz.getDeclaredFieldsWithoutCompanion()
        variables.forEach { field ->
            val currentField = declaredFields.find { it.name == field.name }
            assert(currentField != null) { "Can not find the field with name ${field.name}" }
            field.checkField(currentField!!)
        }
    }

    fun checkEnumEntryDefinition(clazz: Class<*>) {
        checkVariables(clazz, this.declaredEnumEntries)
    }

    fun checkFieldsDefinition(clazz: Class<*>, toCheckDeclaredFieldsSize: Boolean = true) {
        if (toCheckDeclaredFieldsSize) {
            assert(clazz.getDeclaredFieldsWithoutCompanion().size == this.declaredFields.size) { "You need to declare the following fields: ${this.getFieldsListPrettyString()}" }
        }
        this.checkFields(clazz)
    }

    fun getJavaClass(): Class<*> {
        val clazz = this.findClassSafe()
        assert(clazz != null) { "You need to add: ${this.getBaseDefinition()}" }
        return clazz!!
    }

    fun checkNoConstructors(clazz: Class<*>) {
        assert(clazz.constructors.isEmpty()) { "The ${getBaseDefinition()} must not have any constructors" }
    }

    fun getObjectInstance(clazz: Class<*>): Any {
        val field = clazz.getInstanceFiled()
        require(field != null) { "Did not find the INSTANCE of the ${getFullName()}" }
        return field.get(clazz) ?: error("Did not get the INSTANCE of the ${getFullName()}")
    }

    fun checkConstructors(clazz: Class<*>, constructorGetters: List<ConstructorGetter>): Constructor<out Any> {
        require(constructorGetters.isNotEmpty())
        val arguments = constructorGetters.map { it.parameterTypes }.toSet()
        require(arguments.size == 1)
        val constructors = mutableListOf<Constructor<*>>()
        constructorGetters.forEach {
            it.getConstructorWithDefaultArguments(clazz)?.let { constructor ->
                constructors.add(constructor)
            }
        }
        assert(constructors.isNotEmpty()) {
            """
                You don't have any constructors with ${arguments.first().size} arguments in the class $name. 
                Please, check the arguments, probably you need to add the default values.
            """
        }
        return constructors.first()
    }

    fun checkDeclaredMethods(clazz: Class<*>) {
        val methods = clazz.methods
        val privateMethods = clazz.declaredMethods
        customMethods.forEach {
            val candidates = if (it.visibility == Visibility.PRIVATE) {
                privateMethods
            } else {
                methods
            }
            val method = candidates.findMethod(it)
            it.checkMethod(method)
        }
    }

    fun findMethod(clazz: Class<*>, method: TestMethod): Method {
        assert(method in customMethods) { "The method ${method.name} was not found in the class ${getFullName()}" }
        return clazz.methods.findMethod(method)
    }

    fun invokeMethodWithoutArgs(invokeData: TestMethodInvokeData, isPrivate: Boolean = false): Any =
        invokeMethodWithoutArgs(
            clazz = invokeData.clazz,
            instance = invokeData.instance,
            javaMethod = invokeData.method,
            isPrivate = isPrivate,
        )

    fun <T> invokeMethodWithoutArgs(clazz: Class<*>, instance: T, javaMethod: Method, isPrivate: Boolean = false) =
        javaMethod.invokeWithoutArgs(clazz, obj = instance, isPrivate = isPrivate)

    fun invokeMethodWithArgs(vararg args: Any, invokeData: TestMethodInvokeData, isPrivate: Boolean = false): Any =
        invokeMethodWithArgs(
            args = args,
            clazz = invokeData.clazz,
            instance = invokeData.instance,
            javaMethod = invokeData.method,
            isPrivate = isPrivate,
        )

    fun <T> invokeMethodWithArgs(
        vararg args: Any, clazz: Class<*>, instance: T, javaMethod: Method, isPrivate: Boolean = false
    ) = javaMethod.invokeWithArgs(*args, clazz = clazz, obj = instance, isPrivate = isPrivate)
}

fun TestClass.findClass(): Class<*> = Class.forName(this.getFullName())

fun TestClass.findClassSafe() = try {
    this.findClass()
} catch (e: ClassNotFoundException) {
    null
}

private fun Class<*>.getVisibility() = this.modifiers.getVisibility()

@Suppress("ReturnCount", "ForbiddenComment")
private fun Class<*>.getClassType(): ClassType {
    if (this.isInterface) {
        if (this.isSamInterface()) {
            return ClassType.SAM_INTERFACE
        }
        return ClassType.INTERFACE
    }
    if (this.isEnum) {
        return ClassType.ENUM
    }
    if (this.isObject()) {
        return ClassType.OBJECT
    }
    // TODO: think about companion object
    return ClassType.CLASS
}

private fun Class<*>.isSamInterface(): Boolean {
    if (methods.size != 1) {
        return false
    }
    return Modifier.isAbstract(methods.first().modifiers)
}

private fun Class<*>.getInstanceFiled() = this.fields.find { it.name == "INSTANCE" }

private fun Class<*>.isObject() = this.fields.all { Modifier.isStatic(it.modifiers) } && this.getInstanceFiled() != null

private fun Class<*>.checkIfIsDataClass(testClass: TestClass) {
    val methods = this.methods
    val methodsNames = methods.map { it.name }
    val dataClassMethods = listOf(
        "copy",
        "equals",
        "hashCode",
        "toString",
    )
    dataClassMethods.forEach { dataClassMethod ->
        assert(dataClassMethod in methodsNames || methodsNames.any{ dataClassMethod in it }) { "${testClass.getFullName()} must be a data class" }
    }
    val (primary, _) = testClass.declaredFields.partition { it.isInPrimaryConstructor }
    val componentNFunctions = methodsNames.filter { "component" in it }
    val componentNErrorMessage =
        "You must put only ${primary.size} fields into the primary constructor: ${primary.joinToString(", ") { it.name }}."
    assert(componentNFunctions.size == primary.size) { componentNErrorMessage }
    primary.forEachIndexed { index, _ ->
        val name = "component${index + 1}"
        assert(name in methodsNames || methodsNames.any { name in it }) { componentNErrorMessage }
    }
}

private fun Class<*>.hasSameVisibilityWith(testClass: TestClass) = this.getVisibility() == testClass.visibility

private fun Class<*>.hasSameClassTypeWith(testClass: TestClass) = this.getClassType() == testClass.classType

fun Class<*>.isSameWith(testClass: TestClass) =
    this.hasSameVisibilityWith(testClass) && this.hasSameClassTypeWith(testClass)

fun Class<*>.toTestClass(name: String, classPackage: String?): TestClass {
    val visibility = this.getVisibility() ?: throwInternalCourseError()
    return TestClass(name, classPackage, visibility, this.getClassType())
}
