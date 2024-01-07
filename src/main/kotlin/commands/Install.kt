package com.neo.properties.commands

import com.github.ajalt.clikt.core.terminal
import com.google.gson.Gson
import com.neo.properties.core.BaseCommand
import com.neo.properties.model.Config
import com.neo.properties.util.extension.readAsProperties
import com.neo.properties.util.extension.tryAddToGitIgnore
import com.neo.properties.util.Constants
import com.neo.properties.util.Instructions
import com.neo.properties.util.extension.promptFile
import java.io.File

/**
 * Install environment control
 * @author Irineu A. Silva
 */
class Install : BaseCommand(help = "Install environment control") {

    private val configFile by lazy {
        path.resolve(Constants.CONFIG_PATH)
    }

    override fun run() {

        if (configFile.exists()) {
            echo("✔ Already installed")
            // TODO: Add option to view and change config
            return
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
            default = path.resolve(Constants.ENVIRONMENT_FOLDER),
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
        path.tryAddToGitIgnore(Constants.CONFIG_PATH)

        return config
    }
}