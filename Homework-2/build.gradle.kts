import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.cqfn.diktat.diktat-gradle-plugin") version "1.2.3"
}

group = "jub.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testApi(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

detekt {
    ignoreFailures = true
    buildUponDefaultConfig = false
}

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("customDetekt") {
    description = "Runs detekt"
    setSource(files("src/main/kotlin", "src/test/kotlin"))
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$projectDir/config/detekt.yml"))
    debug = true
    ignoreFailures = false
    reports {
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("build/")
}

diktat {
    reporter = "html"
    output = "build/reports/diktat.html"

    diktatConfigFile = file("$projectDir/config/diktat.yml")
    inputs {
        include("**/*.kts")
        include("**/*.kt")
    }
}
