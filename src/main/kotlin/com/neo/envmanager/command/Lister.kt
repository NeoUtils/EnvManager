package com.neo.envmanager.command

import com.neo.envmanager.error.EnvironmentNotFound
import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.NoEnvironmentsFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

/**
 * List environments
 * @author Irineu A. Silva
 */
class Lister : Command(
    name = "list",
    help = "List environments"
) {
    private val tag by argument(
        help = "Environment tag"
    ).optional()

    override fun run() {

        requireInstall()

        val environmentsDir = paths.environmentsDir

        val tag = tag ?: run {
            environmentsDir.showAll()
            return
        }

        environmentsDir.showByTag(tag)
    }

    private fun File.showByTag(tag: String) {

        val environment = resolve(tag.json)

        if (!environment.exists()) {

            echoFormattedHelp(EnvironmentNotFound(tag))
            echo(Instructions.SAVE)

            throw Abort()
        }

        environment.readAsMap().forEach { (key, value) ->
            echo("$key = $value")
        }
    }

    private fun File.showAll() {

        val environments = listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) {

            echoFormattedHelp(NoEnvironmentsFound())
            echo(Instructions.SAVE)

            throw Abort()
        }

        environments.forEach { environment ->
            echo(environment.nameWithoutExtension)
        }
    }
}
