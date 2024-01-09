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
    implementation("com.google.code.gson:gson:2.8.9")

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

tasks.jar {
    manifest {
        attributes["Main-Class"] = "${project.group}.MainKt"

        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "NeoUtils",
            )
        )
    }
}

application {
    mainClass.set("${project.group}.MainKt")
}