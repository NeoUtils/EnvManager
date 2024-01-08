package com.neo.properties.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.neo.properties.commands.*
import java.io.File

class Properties : CliktCommand() {

    private val path by option(
        help = "Path of install"
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
        currentContext.obj = path
    }
}