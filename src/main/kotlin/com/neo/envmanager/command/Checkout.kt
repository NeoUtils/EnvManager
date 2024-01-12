package com.neo.envmanager.command

import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.io.File

class Checkout : Command(
    help = "Checkout an environment"
) {

    private val tag by tag()

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