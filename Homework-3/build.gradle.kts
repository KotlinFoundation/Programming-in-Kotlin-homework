import buildutils.configureDetekt
import buildutils.createDetektTask
import buildutils.configureDiktat
import buildutils.createDiktatTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // "libs" produces a false-positive warning, see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    alias(libs.plugins.buildconfig) apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
        runtimeOnly("org.junit.platform:junit-platform-console:1.9.0")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    configureDiktat()
    configureDetekt()
}

createDiktatTask()
createDetektTask()
