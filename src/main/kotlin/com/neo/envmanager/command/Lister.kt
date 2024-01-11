package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.mordant.rendering.TextStyle
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.EnvironmentNotFound
import com.neo.envmanager.error.NoEnvironmentsFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.StyledText
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.spansOf

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

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        val tag = tag ?: run {

            showEnvironmentsAll()

            return
        }

        showEnvironmentByTag(tag)
    }

    private fun showEnvironmentByTag(tag: String) {

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {

            echoFormattedHelp(EnvironmentNotFound(tag))
            echo(Instructions.SAVE)

            throw Abort()
        }

        environment.readAsMap().forEach { (key, value) ->
            terminal.println(
                StyledText(
                    spansOf(
                        key to TextStyle(),
                        Constants.PROPERTY_SEPARATOR to TextStyle(),
                        value to TextStyle(dim = true)
                    )
                )
            )
        }
    }

    private fun showEnvironmentsAll() {

        val environments = paths.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) {

            echoFormattedHelp(NoEnvironmentsFound())
            echo(Instructions.SAVE)

            throw Abort()
        }

        environments.forEach { environment ->

            val name = environment.nameWithoutExtension

            terminal.println(
                StyledText(
                    name,
                    TextStyle(
                        bold = name == config.currentEnv
                    )
                )
            )
        }
    }
}