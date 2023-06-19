plugins {
    val kotlinVersion = "1.7.10"
    kotlin("multiplatform") version kotlinVersion
}

group = rootProject.group
version = rootProject.version

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
    jvm()
}

tasks {
    "build" {
        doLast {
            copy {
                from("$buildDir/processedResources/js/main")
                into("$buildDir/libs/common-types")
            }
            copy {
                from("$buildDir/compileSync/main/productionExecutable/kotlin")
                into("$buildDir/libs/common-types")
                rename { name -> name.replace("${rootProject.name}-common", "index") }
            }
        }
    }
}
