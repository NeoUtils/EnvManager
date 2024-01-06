package com.neo.properties.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.google.gson.Gson
import com.neo.properties.model.Config
import java.io.File
import java.util.Properties

class Save : CliktCommand(help = "Save current environment") {

    private val tag by argument(
        help = "Tag to save"
    )

    private val path by option(
        help = "Path of install"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    ).default(File("."))

    override fun run() {

        val environments = path.resolve("environment")

        if (!environments.exists()) {
            echo("Environment not installed")
            return
        }

        val config = Gson().fromJson(
            environments
                .resolve("config.json")
                .readText(),
            Config::class.java
        )

        val properties = File(config.properties)

        if(!properties.exists()) {
            echo("Properties file not found")
            return
        }

        val newEnvironment = environments.resolve(tag)

        newEnvironment.writeText(
            Gson().toJson(
                Properties().apply {
                    load(properties.inputStream())
                }.toMap()
            )
        )
    }
}