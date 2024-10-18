# Task 2. Balanced Search Tree

Implement a generic BST (i.e. AVL, Cartesian tree, Splay). Using it, implement interfaces:
- [`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/)
- [`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/)
- [`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list): [`subList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/sub-list.html) can be __skipped__ or implemented via creating a separate tree, there are no tests for it
- Bonus: [`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/) (you should write tests yourself)

##  Implementation details

Below are _mostly recommendations_ on how to solve the task. It is okay if you approach the task in a different way.

### Access to your implementation

There are several interfaces you have to implement. Tests work with instances of interfaces, not specific classes, via methods declared in `HelperFunctions.kt`. Like `listOf(1, 2)` provides you with `List<Int>`, while under the hood `ArrayList<Int>` (or some other implementation class) is used. You should replace TODOs in `HelperFunctions.kt` with code that returns your implementations.

### Architecture and Inheritance

We expect you to write a self-balancing BST as a base class or interface that is a subtype of the first interface declared in the `Task.kt` file. Apart from methods in the interface, it should have the core functionality of a BST, meaning it should be possible to add and remove nodes with $O(\log(n))$ average or amortized time complexity. Notice that second type parameter `V`, which is the type of values, is not `Comparable`, hence `maximum/minimumValue` returns not the actual maximum or minimum, but the value which is assigned to `maximum/minimumKey` respectively.

Most likely, you will need a separate `Node` class. 

When you have a working BST, you can use it to implement other interfaces. `MutableMap` and `Map` share most of their code, so it is easier to only implement the mutable version, which will obviously implement the read-only version, too.

You can make `List` and `Map` completely separate, or one can be inherited from the other, do what you find more comfortable.

__Note:__ `detekt` limits number of methods in a class. Please implement each interface separately whenever possible, instead of creating a single almighty class.

### Access modifiers

Throughout your solution, pay close attention to visibility and inheritance modifiers. Imagine that you are writing a library that you are going to share with the public, do not forget about `sealed`, `internal` and extension functions.

### Iterators

Iterators get invalidated if the underlying collection was modified after their creation not using them. Example:
```kotlin
val list = mutableListOf(1, 2, 3)
for (item in list) {
    println(item)
    list.removeLast()
}
```
This code will print 1 and then throw `ConcurrentModificationException`, because `for` loop creates an iterator, which is invalidated after an element is removed from the list. At the same time, the code below works, and the list will be left empty as a result:
```kotlin
val list = mutableListOf(1, 2, 3)
val it = list.iterator()
for (item in it) {
    it.remove()
}
println(list) // []
```
To learn how to achieve this, you can look through the methods of [`AbstractList`](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/AbstractList.java) and pay attention to `modCount` property.

### Entries, Keys and Values

The `entries`, `keys`, and `values` properties declared in `MutableMap` and `Map` are backed by the underlying map. It means that they should not store a copy of the tree's data, but provide another way of access to the original collection.

To implement them correctly for `MutableMap`, the subsequent contracts must be adhered to:

1. The structural changes of tree have to be reflected in entry, key, or value collections.
1. When something is removed from such sets, it has to be reflected in the underlying collection. If an element is removed using `entries`, `keys`, `values` properties, then the according node has to be removed from the tree.
1. Despite that the collections are mutable, addition support is not required: you can just throw [`UnsupportedOperationException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception) in its implementation.

Example:
```kotlin
val simpleMap = mutableMapOf(1 to "a", 2 to "b")
val entries = simpleMap.entries
println(entries) // [1=a, 2=b], same as map
simpleMap[3] = "c"
println(entries) // [1=a, 2=b, 3=c], element is present in the entries set
entries.removeIf { it.value == "b" }
println(simpleMap) // {1=a, 3=c}, 2=b was removed from the map
entries.add(SOMETHING) // throws UnsupportedOperationException
```

### Null safety

Try to avoid the not-null assertion operator (`!!`). There are various ways to do that:
```kotlin
fun trueIfAllNotNull(value1: Type?, value2: Type?, value3: Type?): Boolean {
    if (value1 == null) {
        return false
    }
    // now value1 is smart-casted to not-null Type
    val notNullValue2 = value2 ?: return false
    value3?.let { // it here is not-null Type
        return true
    }
    return false
}
```

## Partial Solutions

You may have completed the `List` task, and have not implemented maps yet.
Or you may have completed `Map`, but not `MutableMap`.
In such cases, you need to disable corresponding tests in order for the build to pass CI.
To do that, go to `projectDir/.github/workflows/HW3.yml`. 
There is a list of jobs: `build`, `diktat` and so on. 
You can comment out/delete `map`, `mutable-map` or `list` (the whole block, not just the name) if you have not done that part yet.

## Detekt and Diktat
To run detekt: `gradlew customDetekt`  
To run diktat: `gradlew diktatCheck`  
Or find corresponding Gradle tasks in IDEA's Gradle toolbar in 'Tasks > verification'.  
Reports of both detekt and diktat can be found as HTML files in '$projectDir/build/reports'.  
__Note:__ if you disagree with some diktat or detekt rule, you can suppress it and write down your reasoning in the PR. It is okay to sometimes bend the rules.

**For the instructor, how to grade an assignment:**

1 point for each item + 2 points for general approach and code architecture.
