package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.properties.model.Config
import java.io.File
import java.util.*

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

        val environments = getOrCreateEnvironmentsFolder()

        val properties = getPropertiesFile()

        createConfigFile(environments, properties)

        val count = Properties().apply {
            load(properties.inputStream())
        }.count()

        if (count == 1) {
            echo("\n❗ Properties file contains $count properties.")
            echo("Use \"properties save -tag\" to save as an environment.")
        }

        echo("\n✔ Installation complete")
    }

    private fun createConfigFile(environments: File, properties: File) {
        environments
            .resolve("config.json")
            .writeText(
                Gson().toJson(
                    Config(
                        properties = properties.absolutePath
                    )
                )
            )
    }

    private fun createProperties(properties: File) {
        properties.createNewFile()
        echo("✔ Properties file created")

        if (YesNoPrompt("Add to gitignore?", terminal).ask() == true) {
            File(properties.parent, ".gitignore").appendText("\n${properties.name}")
            echo("✔ Added properties file to .gitignore")
        }
    }

    private fun getPropertiesFile(): File {

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
                echo("❗ Properties file not found")
                createProperties(file)
            }

            return file
        }
    }

    private fun getOrCreateEnvironmentsFolder(): File {
        val file = File(path, "environment")

        if (file.exists()) {
            echo("✔ Environment folder found")
            return file
        }

        file.mkdirs()

        echo("\n✔ Created environment folder")

        if (YesNoPrompt("Add to gitignore?", terminal).ask() == true) {
            File(path, ".gitignore").appendText("\n\\environment")
            echo("✔ Added environment folder to .gitignore")
        }

        return file
    }
}