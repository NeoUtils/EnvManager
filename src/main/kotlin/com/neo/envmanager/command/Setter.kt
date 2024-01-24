package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.extension.*
import java.io.File

class Setter : Command(
    name = "set",
    help = "Set properties to environment",
) {

    private val properties by argument(
        help = "Properties to set, separated by space",
        helpTags = mapOf("KEY=VALUE" to "Property"),
    ).properties(required = true)

    private val tag by option(
        names = arrayOf("-t", "--tag"),
        help = "Environment tag"
    )

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Set properties to all environments"
    ).flag()

    private val noSave by option(
        names = arrayOf("-n", "--no-save"),
        help = "Do not save properties to environment"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        saveInTarget()

        if (noSave) return

        if (all) {
            saveInAllEnvironments()
            return
        }

        saveInEnvironment()
    }

    private fun saveInTarget() {
        val target = File(config.targetPath)

        val updatedProperties = target.readAsProperties() + properties

        target.writeText(
            updatedProperties
                .entries
                .joinToString(separator = "\n")
        )
    }

    private fun saveInAllEnvironments() {

        val environments = paths.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) throw NoEnvironmentsFound()

        for (environment in environments) {

            environment.writeText(
                Gson().toJson(
                    environment.readAsMap() + properties
                )
            )
        }
    }

    private fun saveInEnvironment() {

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        environment.writeText(
            Gson().toJson(
                environment.readAsMap() + properties
            )
        )
    }
}
