package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.com.neo.envmanager.util.extension.jsonFiles
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.success
import extension.getOrNull
import extension.ifFailure

class Delete : CliktCommand(
    help = "Delete one or more environments"
) {

    private val tags by argument(
        help = "Environment tags to delete, separated by space"
    ).multiple()

    private val all by option(
        names = arrayOf("-a", "--all"),
        help = "Delete all environments"
    ).flag()

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        if (all) {
            deleteAll()
            return
        }

        deleteByTags()
    }

    private fun deleteByTags() {

        if (tags.isEmpty()) {
            throw SpecifyEnvironmentError(multiple = true)
        }

        val environments = tags.mapNotNull { tag ->
            Environment.getSafe(
                dir = installation.environmentsDir,
                tag = tag
            ).ifFailure {
                echoFormattedHelp(error = it)
            }.getOrNull()
        }

        if (environments.isEmpty()) throw Abort()

        environments.forEach { it.file.delete() }

        val currentTag = installation.config.currentEnv ?: return

        val currentHasDeleted = environments.any {
            it.file.nameWithoutExtension == currentTag
        }

        if (currentHasDeleted) {
            clearCurrentEnvironment()
        }
    }

    private fun deleteAll() {

        val environments = installation
            .environmentsDir
            .jsonFiles()
            .ifEmpty { throw NoEnvironmentsFound() }

        val mustDeleteMessage = "Delete all ${environments.size} environment?"

        if (YesNoPrompt(mustDeleteMessage, terminal).ask() != true) throw Cancel()

        environments.forEach { it.delete() }

        clearCurrentEnvironment()

        echo(success(text = "All environments deleted"))
    }

    private fun clearCurrentEnvironment() {
        installation.config.update {
            it.copy(
                currentEnv = null
            )
        }
    }
}
