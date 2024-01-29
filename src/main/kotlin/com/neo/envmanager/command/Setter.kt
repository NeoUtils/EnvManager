package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.extension.properties
import com.neo.envmanager.util.extension.requireInstall
import java.util.*

class Setter : Command(
    name = "set",
    help = "Set one or more properties",
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

    private val targetOnly by option(
        names = arrayOf("-o", "--target-only"),
        help = "Set properties to target only"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (targetOnly) {
            saveInTarget()
            return
        }

        if (all) {
            saveInAllEnvironments()
            return
        }

        saveInEnvironment()
    }

    private fun saveInTarget() {

        Target(config.targetPath).add(
            Properties().apply {
                putAll(properties)
            }
        )
    }

    private fun saveInAllEnvironments() {

        val environments = paths.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) throw NoEnvironmentsFound()

        environments.forEach { Environment(it).add(properties.toMap()) }

        val mustCheckout = environments.any {
            it.nameWithoutExtension == config.currentEnv
        }

        if (mustCheckout) {
            checkout(tag = config.currentEnv ?: return)
        }
    }

    private fun saveInEnvironment() {

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment.getOrCreate(paths.environmentsDir, tag)

        environment.add(properties.toMap())

        // Checkout when set in current environment
        if (tag == config.currentEnv) {
            checkout(tag)
        }
    }

    private fun checkout(tag: String) {

        val target = Target(config.targetPath)

        val environment = Environment.getOrCreate(paths.environmentsDir, tag)

        target.write(
            environment
                .read()
                .toProperties()
        )
    }
}
