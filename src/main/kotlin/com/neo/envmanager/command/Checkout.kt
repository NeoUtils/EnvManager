package com.neo.envmanager.command

import com.neo.envmanager.error.EnvironmentNotFound
import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

class Checkout : Command(
    help = "Checkout an environment"
) {

    private val tag by argument(
        help = "Tag of environment"
    )

    override fun run() {

        val config = requireInstall()

        val target = File(config.targetPath)

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        target.writeText(
            environment
                .readAsMap()
                .entries
                .joinToString(separator = "\n")
        )

        paths.configFile.writeText(
            Gson().toJson(
                config.copy(
                    currentEnv = tag
                )
            )
        )
    }
}