package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.core.Command
import com.neo.envmanager.model.Environment
import com.neo.envmanager.util.extension.requireInstall

class Copy : Command(
    help = "Copy an environment"
) {
    private val tag by argument(
        help = "Environment tag"
    )

    private val newTag by argument(
        help = "New environment tag"
    )

    override fun run() {

        requireInstall()

        Environment.get(
            paths.environmentsDir,
            tag
        ).copyTo(newTag)
    }
}