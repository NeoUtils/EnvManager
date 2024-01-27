package com.neo.envmanager.command

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
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.deleteChildren
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.success

class Delete : Command(
    help = "Delete one or more environments"
) {

    private val tags by argument(
        help = "Environment tags to delete, separated by space"
    ).multiple()

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Delete all environments"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (all) {
            deleteAll()
            return
        }

        if (tags.isEmpty()) {
            throw SpecifyEnvironmentError(multiple = true)
        }

        if (tags.size == 1) {
            singleDelete()
            return
        }

        multipleDelete()
    }

    private fun singleDelete() {

        val tag = tags.single()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        environment.delete()

        val currentTag = config.currentEnv ?: return

        if (tag == currentTag) {
            clearCurrentEnvironment()
        }
    }

    private fun multipleDelete() {

        for (tag in tags) {

            val environment = paths.environmentsDir.resolve(tag.json)

            if (!environment.exists()) {
                echoFormattedHelp(EnvironmentNotFound(tag))
                continue
            }

            environment.delete()
        }

        val currentTag = config.currentEnv ?: return

        if (tags.contains(currentTag)) {
            clearCurrentEnvironment()
        }
    }

    private fun deleteAll() {

        val count = paths.environmentsDir.listFiles()?.size ?: 0

        if (YesNoPrompt("Delete all $count environment?", terminal).ask() != true) throw Cancel()

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
