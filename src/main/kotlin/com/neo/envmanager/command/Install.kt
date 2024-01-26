package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.google.gson.Gson
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.promptFile
import com.neo.envmanager.core.Command
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.success
import java.io.File

class Install : Command(help = "Install environment control") {

    private val force by option(
        "-f",
        "--force",
        help = "Force installation"
    ).flag()

    override fun run() {

        if (!force && paths.isInstalled()) {
            echo(success(text = "Already installed"))
            throw Abort()
        }

        paths.installationDir.mkdir()

        createGitIgnore()

        finished(createConfig())
    }

    private fun finished(config: Config) {

        echo(success(text = "Installed"))

        val properties = File(
            config.targetPath
        ).readAsProperties()

        if (properties.isNotEmpty()) {
            terminal.println()
            echo(message = "! ${properties.count()} properties found.")
            echo(Instructions.SAVE)
        }
    }

    private fun createConfig(): Config {

        val target = terminal.promptFile(
            text = "Environment properties file",
            mustExist = true,
            canBeDir = false,
        )

        return Config(
            targetPath = target.path
        ).also {
            paths.configFile.writeText(
                Gson().toJson(it)
            )
        }
    }

    private fun createGitIgnore() {
        paths.installationDir
            .resolve(Constants.DOT_GITIGNORE)
            .writeText("*")
    }
}
