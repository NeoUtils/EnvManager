package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.*

class Remove : Command(
    help = "Remove an environment"
) {

    private val tag by tag().optional()

    private val all by option(
        "-a", "--all",
        help = "Remove all environments"
    ).flag()

    lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (all) {
            removeAll()
            return
        }

        removeEnvironment()
    }

    private fun removeEnvironment() {

        val tag = tag ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) throw EnvironmentNotFound(tag)

        environment.delete()

        if (tag == config.currentEnv) {
            clearCurrentEnvironment()
        }
    }

    private fun removeAll() {

        if (YesNoPrompt("Remove all environments?", terminal).ask() != true) throw Cancel()

        paths.environmentsDir.deleteChildren()

        clearCurrentEnvironment()

        echo(success(text = "All environments removed"))
    }

    private fun clearCurrentEnvironment() {
        paths.configFile.writeText(
            Gson().toJson(
                config.copy(
                    currentEnv = null
                )
            )
        )
    }
}
