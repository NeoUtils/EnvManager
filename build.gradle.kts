import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    id("application")
}

group = "com.neo.envmanager"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {

    // Mockk
    testImplementation("io.mockk:mockk:1.12.0")

    // Resource
    implementation("com.github.NeoUtils:Resource:1.1.0")

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