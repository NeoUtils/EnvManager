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

class Delete : Command(
    help = "Delete an environment"
) {

    private val tag by tag().optional()

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Delete all environments"
    ).flag()

    lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (all) {
            deleteAll()
            return
        }

        deleteEnvironment()
    }

    private fun deleteEnvironment() {

        val tag = tag ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) throw EnvironmentNotFound(tag)

        environment.delete()

        if (tag == config.currentEnv) {
            clearCurrentEnvironment()
        }
    }

    private fun deleteAll() {

        if (YesNoPrompt("Delete all environments?", terminal).ask() != true) throw Cancel()

        paths.environmentsDir.deleteChildren()

        clearCurrentEnvironment()

        echo(success(text = "All environments deleted"))
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
