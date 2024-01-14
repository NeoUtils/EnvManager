package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.com.neo.envmanager.exception.error.EnvironmentAlreadyExists
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag

class Rename : Command(
    help = "Rename environment"
) {

    private val tag by tag()

    private val name by argument(
        help = "New environment name"
    )

    override fun run() {

        requireInstall()

        val oldEnvironment = paths.environmentsDir
            .resolve(tag.json)
            .takeIf {
                it.exists()
            } ?: throw EnvironmentNotFound(tag)

        val newEnvironment =
            paths.environmentsDir
                .resolve(name.json)
                .takeIf {
                    !it.exists()
                } ?: throw EnvironmentAlreadyExists(name)

        oldEnvironment.renameTo(newEnvironment)
    }
}