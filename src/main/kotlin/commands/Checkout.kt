package com.neo.properties.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.properties.core.Command
import com.neo.properties.errors.EnvironmentNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.readAsMap
import com.neo.properties.util.extension.requireInstall

class Checkout : Command(
    help = "Checkout an environment"
) {

    private val tag by argument(
        help = "Tag to save"
    )

    override fun run() {

        val (target, environments) = requireInstall().withFile()

        val environment = environments.resolve(tag.json)

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