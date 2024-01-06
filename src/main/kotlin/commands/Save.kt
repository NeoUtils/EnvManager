package com.neo.properties.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.google.gson.Gson
import com.neo.properties.Constants
import com.neo.properties.util.extension.asProperties
import com.neo.properties.util.extension.getConfig
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

        val environments = path.resolve(Constants.ENVIRONMENT_FOLDER)

        if (!environments.exists()) {
            echo("✖ Properties not installed")
            echo("Install with \"properties install\"")
            return
        }

        val config = path.getConfig()

        val properties = File(config.properties)

        if (!properties.exists()) {
            throw CliktError("Properties file not found")
        }

        environments.resolve(tag).writeText(
            Gson().toJson(
                properties.asProperties().toMap()
            )
        )

        echo("✔ Properties saved with tag \"$tag\"")
    }
}