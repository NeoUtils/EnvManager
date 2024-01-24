package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.util.extension.*
import java.io.File

class Setter : Command(
    name = "set",
    help = "Set properties to environment",
) {

    private val tag by option(
        names = arrayOf("-t", "--tag"),
        help = "Environment tag"
    )

    private val properties by argument(
        help = "Properties to set, separated by space",
        helpTags = mapOf("KEY=VALUE" to "Property"),
    ).properties(required = true)

    private val noSave by option(
        names = arrayOf("-n", "--no-save"),
        help = "Do not save properties to environment"
    ).flag()

    override fun run() {

        val config = requireInstall()

        val target = File(config.targetPath)

        val updatedProperties = target.readAsProperties() + properties

        target.writeText(
            updatedProperties
                .entries
                .joinToString(separator = "\n")
        )

        if (noSave) return

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        environment.writeText(
            Gson().toJson(
                environment.readAsMap() + properties
            )
        )
    }
}
