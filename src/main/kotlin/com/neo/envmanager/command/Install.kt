package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.core.Command
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.promptFile
import com.neo.envmanager.util.extension.success

class Install : Command(help = "Install environment control") {

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

    override fun run() {

        if (!mustForce && paths.isInstalled()) {
            echo(success(text = "Already installed"))
            throw Abort()
        }

        paths.installationDir.mkdir()

        createGitIgnore()

        finished(createConfig())
    }

    private fun finished(config: Config) {

        echo(success(text = "Installed"))

        val properties = Target(
            config.targetPath
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
            targetPath = target.path
        ).update()
    }

    private fun createGitIgnore() {
        paths.installationDir
            .resolve(Constants.DOT_GITIGNORE)
            .writeText("*")
    }
}
