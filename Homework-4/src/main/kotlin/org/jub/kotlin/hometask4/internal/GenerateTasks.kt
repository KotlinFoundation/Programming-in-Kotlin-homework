package org.jub.kotlin.hometask4.internal

import org.jub.kotlin.hometask4.Color
import org.jub.kotlin.hometask4.CountryPopulation
import java.util.concurrent.Callable
import kotlin.random.Random

internal fun longSleep() = Thread.sleep(5000)
internal fun shortSleep() = Thread.sleep(300)
internal fun randomSleep() = Thread.sleep(Random.nextLong(300, 5000))
internal fun sometimesThrow() {
    val p = Random.nextLong(300, 5000)
    if (p > 4000) {
        Thread.sleep(p / 2)
        throw Exception("Something went wrong")
    }
    Thread.sleep(Random.nextLong(300, 5000))
}
internal val actions = listOf(::longSleep, ::shortSleep, ::randomSleep, ::sometimesThrow)

internal fun <T> generateTasks(block: () -> T): List<Callable<T>> = actions.map {
    Callable<T> {
        it.invoke()
        block()
    }
}

internal val verbs = listOf("love", "have a passion for", "enjoy", "take great pleasure in")
internal val nouns = listOf("life", "Kotlin", "learning new things")

internal val stringTasks = generateTasks { "I ${verbs.random()} ${nouns.random()}!" }

internal val enumTasks = generateTasks { Color.values().random() }

internal val intTasks = generateTasks { Random.nextInt(-23, 666) }

internal infix fun String.with(pop: Int) = CountryPopulation(this, pop)
internal val countries = listOf(
    "China" with 1_412_600_000,
    "India" with 1_375_586_000,
    "USA" with 333_312_556,
    "Indonesia" with 275_773_800,
    "Pakistan" with 235_825_000
)

internal val dataClassTasks = generateTasks { countries.random() }
