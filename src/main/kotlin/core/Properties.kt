package com.neo.properties.core

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.properties.commands.*
import com.neo.properties.model.Paths
import java.io.File

class Properties : CliktCommand(invokeWithoutSubcommand = true) {

    private val version by option(
        names = arrayOf("-v", "--version"),
        help = "Show version"
    ).flag()

    private val project by option(
        help = "Project path"
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
            Remove()
        )
    }

    override fun run() {

        if (version) throw PrintCompletionMessage("1.0-DEV")

        currentContext.obj = Paths(
            projectDir = project
        )
    }
}