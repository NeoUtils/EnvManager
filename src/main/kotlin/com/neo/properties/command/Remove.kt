package com.neo.properties.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.properties.core.Command
import com.neo.properties.error.EnvironmentNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.requireInstall

class Remove : Command(
    help = "Remove an environment"
) {

    private val tag by argument(
        help = "Tag to remove"
    )

    override fun run() {

        requireInstall()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        environment.delete()
    }
}