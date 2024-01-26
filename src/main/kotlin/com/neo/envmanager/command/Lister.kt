package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.widgets.HorizontalRule
import com.github.ajalt.mordant.widgets.Text
import com.neo.envmanager.com.neo.envmanager.exception.error.NoCurrentEnvironment
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.*
import java.io.File

class Lister : Command(
    name = "list",
    help = "List environments"
) {
    private val tag by tag().optional()

    private val current by option(
        "-c",
        "--current",
        help = "Show current environment"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (current) {

            val tag = config.currentEnv ?: throw NoCurrentEnvironment()

            echo(terminal.theme.info(text = "! Environment: $tag\n"))

            showEnvironmentByTag(tag)

            return
        }

        if (tag != null) {

            showEnvironmentByTag(tag!!)

            return
        }

        showEnvironmentsAll()
    }

    private fun showEnvironmentByTag(tag: String) {

        val environment = paths.environmentsDir.resolve(tag.json)

        if (!environment.exists()) {

            echoFormattedHelp(EnvironmentNotFound(tag))
            echo(Instructions.SAVE)

            return
        }

        environment.readAsMap().forEach { (key, value) ->

            val property = key +
                    Constants.PROPERTY_SEPARATOR +
                    TextStyles.dim(value)

            terminal.println(Text(property))
        }
    }

    private fun showEnvironmentsAll() {

        val environments = paths.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }

        if (environments.isNullOrEmpty()) {

            echoFormattedHelp(NoEnvironmentsFound())
            echo(Instructions.SAVE)

            return
        }

        environments.forEach { environment ->

            val name = environment.nameWithoutExtension

            terminal.println(
                Text(
                    if (name == config.currentEnv) {
                        TextStyles.bold(getCurrentName(environment))
                    } else {
                        name
                    }
                )
            )
        }
    }

    private fun getCurrentName(environment: File): String {

        val target = File(config.targetPath)

        if (environment.readAsMap() == target.readAsProperties()) {
            return environment.nameWithoutExtension
        }

        return environment.nameWithoutExtension + "*"
    }
}