package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.properties.model.Config
import com.neo.properties.util.Constants
import com.neo.properties.util.MapTypeToken
import java.io.File
import java.util.*

context(CliktCommand)
fun File.tryAddToGitIgnore(path: String) {

    val gitIgnore = resolve(".gitignore")

    if (!gitIgnore.exists()) return

    if (gitIgnore.readLines().contains(path)) return

    if (YesNoPrompt("Add to gitignore?", terminal).ask() == true) {

        if (!gitIgnore.readText().contains("## Properties ##")) {
            gitIgnore.appendText("\n\n## Properties ##")
        }

        gitIgnore.appendText("\n$path")
        echo("✔ Added to .gitignore")
    }
}

fun File.readAsProperties(): Properties {
    return Properties().apply {
        load(inputStream())
    }
}

context(CliktCommand)
fun File.getConfig(): Config {

    val configFile = resolve(Constants.CONFIG_PATH)

    if (!configFile.exists()) {
        throw CliktError("Config file not found")
    }

    return configFile.asConfig()
}

fun File.asConfig(): Config {

    return Gson().fromJson(
        readText(),
        Config::class.java
    )
}

fun File.readAsMap(): Map<String, String> {

    return Gson().fromJson(
        readText(),
        MapTypeToken.type
    )
}

fun File.isJson(): Boolean {
    return extension == ".json"
}