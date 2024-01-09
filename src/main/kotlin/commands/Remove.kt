package com.neo.properties.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.properties.core.Command
import com.neo.properties.error.EnvironmentNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.requireInstall
import java.io.File

class Remove: Command(
    help = "Remove an environment"
) {

    private val tag by argument(
        help = "Tag to remove"
    )

    override fun run() {

        requireInstall()

        val environment = environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        environment.delete()
    }
}