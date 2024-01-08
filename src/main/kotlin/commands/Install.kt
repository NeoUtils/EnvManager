package com.neo.properties.commands

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.properties.core.Command
import com.neo.properties.model.Config
import com.neo.properties.util.extension.readAsProperties
import com.neo.properties.util.Constants
import com.neo.properties.util.Instructions
import com.neo.properties.util.extension.promptFile
import java.io.File

/**
 * Install environment control
 * @author Irineu A. Silva
 */
class Install : Command(help = "Install environment control") {

    private val configFile by lazy {
        pathDir.resolve(Constants.CONFIG_FILE_PATH)
    }

    override fun run() {

        if (configFile.exists()) {
            echo("✔ Already installed")
            // TODO: Add option to view and change config
            throw Abort()
        }

        val config = createConfig()

        echo("\n✔ Installation complete")

        showTips(config)
    }

    private fun showTips(config: Config) {

        val properties = File(
            config.targetPath
        ).readAsProperties()

        if (properties.isNotEmpty()) {
            echo("\n! Your current environment contains ${properties.count()} properties.")
            echo(Instructions.SAVE)
        }
    }

    private fun createConfig(): Config {

        val environments = terminal.promptFile(
            text = "Path to environments",
            default = pathDir.resolve(Constants.DEFAULT_ENVIRONMENT_FOLDER_PATH),
            canBeFile = false
        )

        echo()

        val target = terminal.promptFile(
            text = "Path to properties file",
            mustExist = true,
            canBeDir = false,
        )

        val config = Config(
            targetPath = target.path,
            environmentsPath = environments.path
        )

        configFile.writeText(
            Gson().toJson(
                config
            )
        )

        echo("\n✔ Config file created")

        config.withFile().addFilesToGitIgnore()

        return config
    }

    private fun Config.File.addFilesToGitIgnore() {

        val gitignore = pathDir.resolve(Constants.DOT_GITIGNORE)

        if (!gitignore.exists()) return // The project is not versioned in git

        if (gitignore.readLines().contains(Constants.CONFIG_FILE_PATH)) return // Already added

        if (YesNoPrompt("\nAdd created files to gitignore?", terminal).ask() == true) {

            gitignore.appendText(
                buildString {
                    append("\n\n")
                    appendLine("## Properties ##")
                    appendLine(environments.toRelativeString(pathDir))
                    append(configFile.toRelativeString(pathDir))
                }
            )

            echo("✔ Added to ${gitignore.toRelativeString(pathDir)}")
        }
    }
}
