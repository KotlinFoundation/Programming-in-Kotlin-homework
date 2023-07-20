import buildutils.configureDetekt
import buildutils.createDetektTask
import buildutils.configureDiktat
import buildutils.createDiktatTask

plugins {
    kotlin("jvm")
    application
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

project.configureDiktat()
project.createDiktatTask()
project.configureDetekt()
project.createDetektTask()

tasks.register("githubWorkflow") {
    group = "verification"
    dependsOn("diktatCheckAll")
    dependsOn("detektCheckAll")
    dependsOn("test")
}
