package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall

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