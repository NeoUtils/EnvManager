package com.neo.envmanager

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintCompletionMessage
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.envmanager.command.*
import com.neo.envmanager.model.Paths
import com.neo.envmanager.util.Package
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

class Envm : CliktCommand(
    invokeWithoutSubcommand = true,
    printHelpOnEmptyArgs = true
) {

    private val version by option(
        names = arrayOf("-v", "--version"),
        help = "Show version and exit"
    ).flag()

    private val showConfig by option(
        names = arrayOf("-c", "--show-config"),
        help = "Show config file and exit"
    ).flag()

    private val projectDir by option(
        names = arrayOf("-p", "--path"),
        help = "Project directory"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    ).default(File("."))

    init {
        subcommands(
            Install(),
            Save(),
            Lister(),
            Checkout(),
            Delete(),
            Rename(),
            Setter(),
            Remove(),
        )
    }

    override fun run() {

        if (version) throw PrintCompletionMessage(Package.version)

        currentContext.obj = Paths(
            projectDir = projectDir
        )

        if (showConfig) printConfigAndExit()
    }

    private fun printConfigAndExit() {

        val config = requireInstall()

        val stringBuilder = StringBuilder("target: ${config.targetPath}")

        config.currentEnv?.let { stringBuilder.append("\ncurrent: $it") }

        throw PrintCompletionMessage(stringBuilder.toString())
    }
}