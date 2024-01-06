plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.neo.properties"
version = "1.0-DEV"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}