package com.neo.properties.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.properties.core.Command
import com.neo.properties.error.EnvironmentNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.readAsMap
import com.neo.properties.util.extension.requireInstall
import java.io.File

class Checkout : Command(
    help = "Checkout an environment"
) {

    private val tag by argument(
        help = "Tag of environment"
    )

    override fun run() {

        val target = File(requireInstall().targetPath)

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        target.writeText(
            environment
                .readAsMap()
                .entries
                .joinToString(separator = "\n")
        )
    }
}