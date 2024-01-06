import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    id("application")
}

group = "com.neo.properties"
version = "1.0-DEV"

repositories {
    mavenCentral()
}

dependencies {

    // Gson
    implementation("com.google.code.gson:gson:2.8.8")

    // Clikt
    implementation("com.github.ajalt.clikt:clikt:4.2.1")

    // Tests
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

application {
    mainClass.set("com.neo.properties.MainKt")
}