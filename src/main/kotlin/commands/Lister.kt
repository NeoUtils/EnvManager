package com.neo.properties.commands

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.neo.properties.core.BaseCommand
import com.neo.properties.errors.EnvironmentNotFound
import com.neo.properties.errors.NoEnvironmentsFound
import com.neo.properties.util.Constants
import com.neo.properties.util.Instructions
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.readAsMap
import com.neo.properties.util.extension.requireInstall
import java.io.File

/**
 * List environments
 * @author Irineu A. Silva
 */
class Lister : BaseCommand(
    name = "list",
    help = "List environments"
) {
    private val tag by argument(
        help = "Environment tag"
    ).optional()

    override fun run() {

        val environmentsDir = File(requireInstall().environmentsPath)

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

            return
        }

        environments.forEach { environment ->
            echo(environment.nameWithoutExtension)
        }
    }
}
