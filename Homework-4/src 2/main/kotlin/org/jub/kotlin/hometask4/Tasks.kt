package org.jub.kotlin.hometask4

import org.jub.kotlin.hometask4.internal.dataClassTasks
import org.jub.kotlin.hometask4.internal.enumTasks
import org.jub.kotlin.hometask4.internal.intTasks
import org.jub.kotlin.hometask4.internal.stringTasks

enum class Color {
    BLACK,
    BLUE,
    CYAN,
    DARK_GRAY,
    GRAY,
    GREEN,
    LIGHT_GRAY,
    MAGENTA,
    ORANGE,
    PINK,
    RED,
    WHITE,
    YELLOW,
    ;
}

data class CountryPopulation(val country: String, val population: Int)

val tasks = intTasks + stringTasks + enumTasks + dataClassTasks
