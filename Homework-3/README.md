# Task 3. Gradle broken build homework

In this task you need to fix this project:

1) Oh no, error `Configuration with name 'implementation' not found.` is thrown for all `build.gradle.kts` files. What is the mistake?
2) This project has 2 modules: `module1` and `module2`, but why doesn’t IntelliJ IDEA highlight `module1`?
3) `module2` contains the utils package with the `getStringRepresentation` function. We want to call this function from `module1`, but we get an error when we try. How would you fix this error?
4) It seems some imports are unresolved in [QuickSortTest.kt](./module1/src/test/kotlin/QuickSortTest.kt). What is the real reason?
5) `module1` contains an implementation of the `quickSort` algorithm. The current version has several mistakes (the right version is commented). If you try to run tests locally, they will fail, but CI says everything is ok, because `./gradlew test` does not fail. What is the reason? You need to change something in order for the task `./gradlew test` to run tests.
6) The project has a [libs.versions.toml](./gradle/libs.versions.toml) file to store versions, plugins, and dependencies. You need to move all dependencies from the `build.gradle.kts` file into this toml file.

**For the instructor, how to grade an assignment:**

Each fix earns 0.5 points. In total, a student can get 3 points for this homework.
