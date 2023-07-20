[![Build](https://github.com/Belosnegova/Programming-in-Kotlin-homework-3/actions/workflows/Build.yml/badge.svg)](https://github.com/Belosnegova/Programming-in-Kotlin-homework-3/actions/workflows/Build.yml)

# Task 3. Gradle broken build homework

In this task you need to fix this project:
1) Oh no, error `Configuration with name 'implementation' not found.` is thrown for all `build.gradle.kts` files.
   What is the mistake?
2) This project has two modules: `module1` and `module2`, but why the IDEA does not highlight `module1`?
3) The `module2` contains the `utils` package with the `getStringRepresentation` function.
   It must be used in the `module1`, but something went wrong... You need to fix it.
4) It seems some imports are unresolved in [QuickSortTest.kt](./module1/src/test/kotlin/QuickSortTest.kt).
   What is the real reason?
5) The `module1` contains an implementation of `quickSort` algorithm. The current version has several mistakes (the right version is commented).
   If you try to run tests locally, they will fail, but CI says everything is ok, because `./gradlew test` does not fail.
   What is the reason? You need to change something in order for the task `./gradlew test` to run tests.
6) The project has [libs.versions.toml](./gradle/libs.versions.toml) file to store versions, plugins, and dependencies.
   You need to move all dependencies from the `build.gradle.kts` file into this `toml` file.

Each fix costs 0.5 points, in total you can get 3 points for this homework.
