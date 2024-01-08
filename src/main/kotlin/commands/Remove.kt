package com.neo.properties.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.properties.core.BaseCommand
import com.neo.properties.errors.EnvironmentNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.requireInstall
import java.io.File

class Remove: BaseCommand(
    help = "Remove an environment"
) {

    private val tag by argument(
        help = "Tag to remove"
    )

    override fun run() {

        val environments = File(requireInstall().environmentsPath)

        val environment = environments.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        environment.delete()
    }
}