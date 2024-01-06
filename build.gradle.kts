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

application {
    mainClass.set("com.neo.properties.MainKt")
}