import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "jetbrains.kotlin.course"
version = "0.0.1-SNAPSHOT"

fun properties(key: String) = project.findProperty(key).toString()

@Suppress("DSL_SCOPE_VIOLATION") // "libs" produces a false-positive warning, see https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    java
    val kotlinVersion = "1.7.10"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("org.jetbrains.kotlin.multiplatform") version kotlinVersion apply false
    id("org.springframework.boot") version "2.7.3" apply false
    id("io.spring.dependency-management") version "1.0.13.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false
    id("io.gitlab.arturbosch.detekt") version "1.21.0"

    id("org.siouan.frontend-jdk11") version "6.0.0"
}

val detektReportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/merge.sarif"))
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }
}

val frontendSuffix = "Frontend"
val server = "Server"

configure(subprojects.filter { it.name != "common" && frontendSuffix !in it.name }) {
    apply<io.gitlab.arturbosch.detekt.DetektPlugin>()

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config = rootProject.files("detekt.yml")
        buildUponDefaultConfig = true
        debug = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
        finalizedBy(detektReportMerge)
        reports.sarif.required.set(true)
        detektReportMerge.get().input.from(sarifReportFile)
    }

    tasks.getByPath("detekt").onlyIf { project.hasProperty("runDetekt") }

    val jvmVersion = "11"

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = jvmVersion
            }
        }

        withType<JavaCompile> {
            sourceCompatibility = jvmVersion
            targetCompatibility = jvmVersion
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}

fun String.getGameName(suffix: String) = substring(0 until indexOf(suffix))

configure(subprojects.filter { server in it.name || "utils" in it.name }) {
    apply {
        plugin("java")
        plugin("kotlin")
    }

    dependencies {
        val junitJupiterVersion = "5.9.0"
        implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-console:1.9.0")
    }
}

configure(subprojects.filter { server in it.name }) {
    val projectName = this.name
    val gameName = projectName.getGameName(server)

    apply {
        plugin("java")
        plugin("kotlin")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    dependencies {
        implementation(project(":common"))
        implementation(project(":utils"))

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
    }

    tasks.named("processResources") {
        dependsOn(":$gameName$frontendSuffix:build")
    }

    sourceSets {
        getByName("main").java.srcDirs("$gameName/src/main/kotlin")
        getByName("main").resources.srcDirs("$gameName/src/main/resources")
        getByName("test").java.srcDirs("$gameName/src/test")
    }
}

configure(subprojects.filter { frontendSuffix in it.name }) {
    val projectName = this.name
    val gameName = projectName.getGameName(frontendSuffix)

    apply {
        plugin("org.siouan.frontend-jdk11")
    }

    val addCommonTypesTask = tasks.register<Exec>("addCommonTypes") {
        outputs.upToDateWhen { false }
        workingDir = projectDir
        commandLine("yarn", "remove", "common-types")
        commandLine("yarn", "add", "common-types@file:$rootDir/common/build/libs/common-types")
    }
    addCommonTypesTask {
        mustRunAfter(":common:build")
    }

    val yarnRunBuildTask = tasks.register<Exec>("yarnRunBuildTask") {
        commandLine("yarn", "run", "build")
    }

    val serveResourcesTask = tasks.register("serveResources") {
        dependsOn(yarnRunBuildTask)
        val serverResources = rootProject.subprojects
            .filter { gameName in it.name && server in it.name }
            .map { "$rootDir/$gameName$server/src/main/resources/static/" }
        val staticFolder = "$rootDir/$gameName$frontendSuffix/build"
        doLast {
            serverResources.forEach { resourcesPath ->
                rootProject.delete(resourcesPath)
                copy {
                    from(staticFolder)
                    into(resourcesPath)
                }
            }
        }
    }

    serveResourcesTask {
        mustRunAfter(":$gameName$frontendSuffix:${yarnRunBuildTask.name}")
    }
}
