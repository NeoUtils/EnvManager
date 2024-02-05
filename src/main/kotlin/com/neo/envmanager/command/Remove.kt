package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.KeyNotFound
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.exception.error.SpecifyKeysError
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.extension.requireInstall
import java.util.*

class Remove : CliktCommand(
    help = "Remove one or more properties"
) {

    private val keys by argument(
        help = "Properties tags to delete, separated by space"
    ).multiple()

    private val tag by option(
        names = arrayOf("-t", "--tag"),
        help = "Specified environment tag; current environment is used by default"
    )

    private val targetOnly by option(
        names = arrayOf("-o", "--target-only"),
        help = "Delete <keys> from target only"
    ).flag()

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Remove <keys> from all environments or remove all properties"
    ).flag()

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        if (all && keys.isEmpty()) {
            clearProperties()
            return
        }

        remove()
    }

    private fun clearProperties() {

        if (targetOnly) {

            // clear target

            val target = Target(installation.config.targetPath)

            target.write(Properties())

            return
        }

        // clear environment

        val tag = tag ?: installation.config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment(installation.environmentsDir, tag)

        val propertiesCount = environment.read().size

        val prompt = "Delete all $propertiesCount properties?"

        if (YesNoPrompt(prompt, terminal).ask() != true) throw Cancel()

        environment.write(emptyMap<Any, Any>())

        if (tag == installation.config.currentEnv) {
            updateTarget(tag)
        }
    }

    private fun remove() {

        if (keys.isEmpty()) {
            throw SpecifyKeysError()
        }

        if (targetOnly) {
            removeFromTarget()
            return
        }

        if (all) {
            removeFromAllEnvironments()
            return
        }

        removeFromEnvironment()
    }

    private fun removeFromAllEnvironments() {

        val environments = installation.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) throw NoEnvironmentsFound()

        val prompt = "Delete these properties from all ${environments.size} environments?"

        if (YesNoPrompt(prompt = prompt, terminal).ask() != true) throw Cancel()

        for (environment in environments.map(::Environment)) {

            val tag = environment.file.nameWithoutExtension

            val properties = environment.read().toMutableMap()

            val keys = keys.mapNotNull { key ->
                if (properties.containsKey(key)) {
                    key
                } else {
                    echoFormattedHelp(KeyNotFound(key, tag))
                    null
                }
            }

            keys.forEach { properties.remove(it) }

            environment.write(properties)

            if (tag == installation.config.currentEnv) {
                updateTarget(tag)
            }
        }
    }

    private fun removeFromEnvironment() {

        val tag = tag ?: installation.config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment(installation.environmentsDir, tag)

        val properties = environment.read().toMutableMap()

        val keys = keys.mapNotNull { key ->
            if (properties.containsKey(key)) {
                key
            } else {
                echoFormattedHelp(KeyNotFound(key))
                null
            }
        }

        if (keys.isEmpty()) throw Abort()

        keys.forEach { properties.remove(it) }

        environment.write(properties)

        // Checkout when set in current environment
        if (tag == installation.config.currentEnv) {
            updateTarget(tag)
        }
    }

    private fun removeFromTarget() {

        val target = Target(installation.config.targetPath)

        val properties = target.read()

        val keys = keys.mapNotNull { key ->
            if (properties.containsKey(key)) {
                key
            } else {
                echoFormattedHelp(KeyNotFound(key))
                null
            }
        }

        if (keys.isEmpty()) throw Abort()

        keys.forEach { properties.remove(it) }

        target.write(properties)
    }

    private fun updateTarget(tag: String) {

        val target = Target(installation.config.targetPath)

        val environment = Environment(installation.environmentsDir, tag)

        target.write(environment.read().toProperties())
    }
}
