package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.model.Environment
import com.neo.envmanager.util.extension.requireInstall

class Rename : CliktCommand(
    help = "Rename an environment"
) {

    private val oldTag by argument(
        help = "Old environment tag"
    )

    private val newTag by argument(
        help = "New environment tag"
    )

    override fun run() {

        val (config, environmentsDir) = requireInstall()

        Environment(environmentsDir, oldTag).renameTo(newTag)

        if (oldTag == config.currentEnv) {
            config.update {
                it.copy(
                    currentEnv = newTag
                )
            }
        }
    }
}