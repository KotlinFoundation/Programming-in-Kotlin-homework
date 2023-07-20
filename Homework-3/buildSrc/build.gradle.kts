plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.20.0")
    implementation("org.cqfn.diktat:diktat-gradle-plugin:1.1.0")
}
