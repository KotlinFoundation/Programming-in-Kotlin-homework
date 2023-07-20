[![Gradle Build](https://github.com/Belosnegova/Programming-in-Kotlin-homework-1/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/Belosnegova/Programming-in-Kotlin-homework-1/actions/workflows/gradle-build.yml)
[![Gradle Build With Detekt](https://github.com/Belosnegova/Programming-in-Kotlin-homework-1/actions/workflows/gradle-build-with-detekt.yml/badge.svg)](https://github.com/Belosnegova/Programming-in-Kotlin-homework-1/actions/workflows/gradle-build-with-detekt.yml)

## Task 1. Object-oriented programming

In this homework task, you need to implement the well-known [Alias](https://en.wikipedia.org/wiki/Alias_(board_game)) game. The result will be a web application that allows you to play Alias with friends. Since the client (front-end) and interaction with the server have already been implemented, you only need to implement the API.

This task consists of two parts – a mandatory one and an optional one. For the mandatory part, you must implement the main API to produce a working version of the application. This will be checked by tests. As an optional part of the task, you can implement the ability to save the current state of the game in files and add an option to start the game when the server is turned off. This can be completed for additional points and will be checked as a result of a manual review.

In total, 5 points are available for this homework – 3 for the mandatory part, and 2 for the optional part.

### How to run the game

To run the application you need to run the `main` function inside the [AliasApplication.kt](./aliasServer/src/main/kotlin/jetbrains/kotlin/course/alias/AliasApplication.kt) file:

![How to run the game](./utils/src/main/resources/images/run/alias_run.png)

Don't forget to _stop all other runs_ by pressing the red square button:

![How to stop the game](./utils/src/main/resources/images/stop/alias_stop.png)

Next you need to open any browser (we recommend using [Google Chrome](https://www.google.com/chrome/) to display elements as in the examples) and open http://localhost:8080/. You will see the main page of the application:

![The main page of the game](./utils/src/main/resources/images/main/alias.png)

If a game from a previous launch is displayed on the screen when starting the game, you need to reset the caches. This can be done with the keyboard shortcut `ctrl` + `shift` + `R` (`command` + `shift` + `R` for macOS).


### The mandatory part (3 points)

If you have a compilation error on any step, please solve the problem and try again. This is expected behavior, since the code requires several classes (`Team`, `Word` and `Card`, and the type alias `GameResult`), but they do not exist yet. You can also add these classes and try again.

1. Create a type alias `Identifier`, an `IdentifierFactory` class, and a `uniqueIdentifier` function in the `jetbrains.kotlin.course.alias.util` package.

- The type alias `Identifier` needs to be an alias for the `Int` type. If you change the type in the future, e.g. create a new class, it will be changed automatically in all places.
- The `IdentifierFactory` class is a class for generating unique identifiers, e.g. identifiers for different game cards or teams. It should have a special `counter` – an `Int` property for storing the last unique number. By default, `counter` should be zero.
  It should have a special `counter` – an `Int` property to store the last unique number. By default, `counter` should be zero.
- The `uniqueIdentifier` function returns a new unique identifier by incrementing the `counter` and returning it.

2. Create a data class `Team` in the `jetbrains.kotlin.course.alias.team` package to store the information about teams.
- It must have two properties in the primary constructor: `id` of `Identifier` type to identify each team and `points` of `Int` type to store the number of points in the game. For points, set the default value `0`.
- It must have an additional property `name`, which initializes automatically as `"Team#${id + 1}"` and will be shown in the leaderboard.

3. The package `jetbrains.kotlin.course.alias.team` already has a regular class `TeamService`. It is responsible for the game logic for the teams. In this task, you need to implement several things to bring the game to life.

- Add a property `identifierFactory` with the type `IdentifierFactory` to generate identifiers for each team. Don't forget to add the default value for it by creating a new instance of the `IdentifierFactory` class.
- Add a companion object to the `TeamService` class and declare the `teamsStorage` variable to store all previous teams. The storage type should be `MutableMap`, which maps `Identifier` to `Team`. Don't forget to initialize it via an empty map.
- Implement the `generateTeamsForOneRound` method. The method must generate a list of teams and also store all of them in the `teamsStorage` map. To create new teams you need to use `identifierFactory` from the `TeamService` class to generate a new ID. We need to create this method to save game results for the leaderboard.

4. Create two classes to work with the cards in the `jetbrains.kotlin.course.alias.card` package.
- A value class `Word` with one `String` `word` property to store a word.
- A data class `Card` to store information for each card. Each card must store an `id` with the `Identifier` type and a list of `words` (`List<Word>`). These properties don't have default values and must also be defined in the primary constructor.

5. The package `jetbrains.kotlin.course.alias.card` already has the regular class `CardService`. You need to add several properties and implement several methods.

- Add a property `identifierFactory` with the type `IdentifierFactory` to generate identifiers for each card. Don't forget to add the default value for it by creating a new instance of the `IdentifierFactory` class.
- Add a property `cards` that stores a list of cards (`List<Card>`). Initialize it by calling the `generateCards` method.
- Add a companion object to the `CardService` class and declare the `WORDS_IN_CARD` const variable to store the number of words for the cards. You need to assign the value `4` to it. Also, declare `cardsAmount` here, which stores the possible number of cards: `words.size / WORDS_IN_CARD`. The project contains a predefined list of words called `words`.
- Add the `toWords` function to the `CardService` class, which is an extension function for `List<String>` and converts each element from this list into `Word`.
- Implement the `generateCards` function, which shuffles the `words` list, splits it into chunks with `WORDS_IN_CARD` words, takes `cardsAmount` chunks for `cardsAmount` cards, and finally creates a new `Card` for each chunk.
- Implement the `getCardByIndex` method, which accepts `index` (an integer number) and the `Card` at this index. If the card does not exist, throw an error.

6. Implement several things in the already defined class `GameResultsService` in the `jetbrains.kotlin.course.alias.results` package.

- Add a type alias `GameResult` referring to `List<Team>` to the `jetbrains.kotlin.course.alias.results` package.
- Add a companion object to the `GameResultsService` and declare the `gameHistory` variable for storing the list of game results (`MutableList<GameResult>`). By default, it must be initialized via an empty list.
- Implement the `saveGameResults` method that adds the `result` to the `gameHistory`. Before adding the `result` you need to check two requirements and throw an error if they are broken: `result` must be not empty and all team IDs from the `result` must be in the `TeamService.teamsStorage`.
- Implement the `getAllGameResults` method that returns the reversed `gameHistory` list.

At the end of this part you will have the following application: 
![The Alias game](./utils/src/main/resources/images/states/alias/state2.gif)

### The optional part (2 points)

As an optional part of this task, you can implement the ability to save the current state of the game in files and add an option to start the game when the server is turned off. To do this, you need to save the following:
-  `gameHistory` from `GameResultsService`
-  `teamsStorage` from `TeamService`
-  The last assigned `id` in `identifierFactory` from `TeamService` and `CardService`
-  The words/cards that have already been used. 

You can simply store them in files, or you can use any format for serialization. When you start the application, you need to extract this information.

### How to check code style

You need to run the `build` Gradle task with the `runDetekt` argument: `./gradlew build -PrunDetekt`