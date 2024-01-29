package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.neo.envmanager.com.neo.envmanager.exception.error.EnvironmentAlreadyExists
import com.neo.envmanager.core.Command
import com.neo.envmanager.model.Environment
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.update

class Rename : Command(
    help = "Rename an environment"
) {

    private val oldTag by argument(
        help = "Old environment tag"
    )

    private val newTag by argument(
        help = "New environment tag"
    )

    override fun run() {

        val config = requireInstall()

        val oldEnvironment = Environment.get(paths.environmentsDir, oldTag)

        val newEnvironment = paths.environmentsDir.resolve(newTag.json)

        if (newEnvironment.exists()) throw EnvironmentAlreadyExists(newTag)

        oldEnvironment.file.renameTo(newEnvironment)

        if (oldTag == config.currentEnv) {
            config.update {
                it.copy(
                    currentEnv = newTag
                )
            }
        }
    }
}