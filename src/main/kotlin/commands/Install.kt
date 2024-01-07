package com.neo.properties.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.properties.model.Config
import com.neo.properties.util.extension.asProperties
import com.neo.properties.util.extension.create
import com.neo.properties.util.extension.tryAddToGitIgnore
import com.neo.properties.util.Constants
import java.io.File

class Install : CliktCommand(help = "Install environment control") {

    private val path by option(
        help = "Path to install"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    ).default(File("."))

    override fun run() {

        echo("\nInstalling in \"${path.absolutePath}\"")

        createEnvironmentsFolder()

        val properties = getOrCreatePropertiesFile()

        createConfigFile(properties)

        echo("\n✔ Installation complete")

        val count = properties.asProperties().count()

        if (count == 1) {
            echo("\n! Properties file contains $count properties.")
            echo("Use \"properties save -[tag]\" to save as an environment.")
        }
    }

    private fun createConfigFile(properties: File) {
        Config(
            properties = properties.absolutePath
        ).create(path)
    }

    private fun createProperties(properties: File) {

        properties.createNewFile()
        echo("✔ Properties file created")

        path.tryAddToGitIgnore(properties.name)
    }

    private fun getOrCreatePropertiesFile(): File {

        while (true) {
            val path = terminal.prompt("\nProperties file")

            if (path.isNullOrEmpty()) {
                echo("✖ Path cannot be empty", err = true)
                continue
            }

            val file = File(path)

            if (file.exists() && file.isDirectory) {
                echo("✖ Path is not a file", err = true)
                continue
            }

            if (file.exists()) {
                echo("✔ Properties file found")
            } else {
                echo("! Properties file not found")
                createProperties(file)
            }

            return file
        }
    }

    private fun createEnvironmentsFolder() {

        val folder = File(path, Constants.ENVIRONMENT_FOLDER)

        if (folder.exists()) {

            echo("✔ Environment folder found")

            return
        }

        folder.mkdirs()
        echo("\n✔ Created environment folder")

        path.tryAddToGitIgnore("\\${Constants.ENVIRONMENT_FOLDER}")

        return
    }
}