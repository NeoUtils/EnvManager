package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.KeyNotFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.exception.error.SpecifyKeysError
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import extension.getOrThrow
import java.util.*

class Remove : Command(
    help = "Remove one or more properties"
) {

    private val keys by argument(
        help = "Properties tags to delete, separated by space"
    ).multiple()

    private val tag by option(
        names = arrayOf("-t", "--tag"),
        help = "Environment tag"
    )

    private val targetOnly by option(
        names = arrayOf("-o", "--target-only"),
        help = "Delete properties from target only"
    ).flag()

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Set properties to all environments"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (all) {
            removeAll()
            return
        }

        removeByKeys()
    }

    private fun removeByKeys() {

        if (keys.isEmpty()) {
            throw SpecifyKeysError()
        }

        if (targetOnly) {
            removeInTarget()
            return
        }

        removeInEnvironment()
    }

    private fun removeAll() {

        if (targetOnly) {

            val target = Target(config.targetPath)

            target.write(Properties())

            return
        }

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment(paths.environmentsDir, tag)

        val propertiesCount = environment.read().size

        if (YesNoPrompt("Delete all $propertiesCount properties?", terminal).ask() != true) throw Cancel()

        // Clear environment
        environment.write(emptyMap<Any, Any>())

        if (tag == config.currentEnv) {
            updateTarget(tag)
        }
    }

    private fun removeInEnvironment() {

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment(paths.environmentsDir, tag)

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
        if (tag == config.currentEnv) {
            updateTarget(tag)
        }
    }

    private fun updateTarget(tag: String) {

        val target = Target(config.targetPath)

        val environment = Environment(paths.environmentsDir, tag)

        target.write(environment.read().toProperties())
    }

    private fun removeInTarget() {

        val target = Target(config.targetPath)

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
}
