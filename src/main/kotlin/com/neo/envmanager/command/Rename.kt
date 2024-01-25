package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.neo.envmanager.com.neo.envmanager.exception.error.EnvironmentAlreadyExists
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag

class Rename : Command(
    help = "Rename environment"
) {

    private val oldTag by argument(
        help = "Old environment tag"
    )

    private val newTag by argument(
        help = "New environment tag"
    )

    override fun run() {

        val config = requireInstall()

        val oldEnvironment = paths.environmentsDir
            .resolve(oldTag.json)
            .takeIf {
                it.exists()
            } ?: throw EnvironmentNotFound(oldTag)

        val newEnvironment =
            paths.environmentsDir
                .resolve(newTag.json)
                .takeIf {
                    !it.exists()
                } ?: throw EnvironmentAlreadyExists(newTag)

        oldEnvironment.renameTo(newEnvironment)

        if (oldTag == config.currentEnv) {
            paths.configFile.writeText(
                Gson().toJson(
                    config.copy(
                        currentEnv = newTag
                    )
                )
            )
        }
    }
}