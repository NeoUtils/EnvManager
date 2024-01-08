package com.neo.properties.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

abstract class BaseCommand(
    name: String? = null,
    help: String
) : CliktCommand(name = name, help = help) {

    val path by option(
        help = "Path of install"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    ).default(File("."))
}