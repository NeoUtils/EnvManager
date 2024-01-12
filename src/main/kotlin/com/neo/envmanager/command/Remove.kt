package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.EnvironmentNotFound
import com.neo.envmanager.error.SpecifyEnvironmentError
import com.neo.envmanager.util.extension.deleteChildren
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag

class Remove : Command(
    help = "Remove an environment"
) {

    private val tag by tag().optional()

    private val all by option(
        "-a", "--all",
        help = "Remove all environments"
    ).flag()

    override fun run() {

        requireInstall()

        if (all) {
            removeAll()
            return
        }

        val tag = tag ?: throw SpecifyEnvironmentError()

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {
            throw EnvironmentNotFound(tag)
        }

        environment.delete()
    }

    private fun removeAll() {

        if (YesNoPrompt("Remove all environments?", terminal).ask() != true) {
            echo("✖ Aborted")
            throw Abort()
        }

        paths.environmentsDir.deleteChildren()

        echo("✔ All environments removed")
    }
}
