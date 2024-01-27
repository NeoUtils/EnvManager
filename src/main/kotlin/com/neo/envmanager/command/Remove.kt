package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.exception.error.SpecifyKeysError
import com.neo.envmanager.exception.error.KeyNotFound
import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

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

        if (keys.isEmpty()) {
            throw SpecifyKeysError()
        }

        if (targetOnly) {
            removeInTarget()
            return
        }

        removeFromEnvironment()
    }

    private fun removeAll() {

        if (targetOnly) {

            val target = File(config.targetPath)

            if (!target.exists()) {
                throw TargetNotFound(target.path)
            }
            
            // Clear target
            target.writeText(text = "")

            return
        }

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        val count = environment.readAsMap().size

        if (YesNoPrompt("Delete all $count properties?", terminal).ask() != true) throw Cancel()

        // Clear environment
        environment.writeText(text = "")

        if (tag == config.currentEnv) {
            checkout(tag)
        }
    }

    private fun removeFromEnvironment() {

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        val properties = environment.readAsMap().toMutableMap()

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

        environment.writeText(
            Gson().toJson(
                properties
            )
        )

        // Checkout when set in current environment
        if (tag == config.currentEnv) {
            checkout(tag)
        }
    }

    private fun checkout(tag: String) {

        val target = File(config.targetPath)

        val environment = paths.environmentsDir.resolve(tag.json)

        target.writeText(
            environment
                .readAsMap()
                .entries
                .joinToString(separator = "\n")
        )
    }

    private fun removeInTarget() {

        val target = File(config.targetPath)

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        val properties = target.readAsProperties()

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

        target.writeText(
            properties
                .entries
                .joinToString(separator = "\n")
        )
    }
}