package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Paths
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.promptFile
import com.neo.envmanager.util.extension.success

class Install : CliktCommand(help = "Install environment control") {

    private val mustForce by option(
        names = arrayOf("-f", "--force"),
        help = "Force installation; useful to fix installation"
    ).flag()

    private val target by option(
        names = arrayOf("-t", "--target"),
        help = "Target (environment properties file)"
    ).file(
        mustExist = true,
        canBeDir = false,
        canBeFile = true
    )

    private val paths by requireObject<Paths>()

    override fun run() {

        if (!mustForce && paths.configFile.exists()) {
            echo(success(text = "Already installed"))
            throw Abort()
        }

        paths.installationDir.mkdir()

        paths.gitIgnoreFile.writeText("*")

        finished(createConfig())
    }

    private fun finished(config: Config) {

        echo(success(text = "Installed"))

        val properties = Target(
            config.targetFile
        ).read()

        if (properties.isNotEmpty()) {
            echo(message = "\n! ${properties.count()} properties found.")
            echo(Instructions.SAVE)
        }
    }

    private fun createConfig(): Config {

        val target = target ?: terminal.promptFile(
            text = "Target (environment properties file)",
            mustExist = true,
            canBeDir = false,
        )

        return Config(
            targetFile = target
        ).update()
    }
}
