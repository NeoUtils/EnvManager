package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.widgets.Text
import com.neo.envmanager.com.neo.envmanager.exception.error.NoCurrentEnvironment
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.Instructions
import com.neo.envmanager.util.extension.readAsMap
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.io.File

class Lister : Command(
    name = "list",
    help = "List environments or properties"
) {
    private val tag by tag().optional()

    private val current by option(
        names = arrayOf("-c", "--current"),
        help = "Show current environment"
    ).flag()

    private val target by option(
        names = arrayOf("-t", "--target"),
        help = "Show target properties"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        if (target) {
            showTarget()
            return
        }

        if (current) {

            val tag = config.currentEnv ?: throw NoCurrentEnvironment()

            echo(terminal.theme.info(text = "> $tag"))

            showEnvironmentByTag(tag)

            return
        }

        if (tag != null) {

            showEnvironmentByTag(tag!!)

            return
        }

        showAllEnvironments()
    }

    private fun showTarget() {

        val target = Target(config.targetPath)

        target.read().forEach { (key, value) ->

            val property = "$key" +
                    Constants.PROPERTY_SEPARATOR +
                    TextStyles.dim("$value")

            terminal.println(Text(property))
        }
    }

    private fun showEnvironmentByTag(tag: String) {

        val environment = runCatching {
            Environment.get(paths.environmentsDir, tag)
        }.getOrElse {

            echoFormattedHelp(EnvironmentNotFound(tag))
            echo(Instructions.SAVE)

            throw Abort()
        }

        environment.read().forEach { (key, value) ->

            val property = key +
                    Constants.PROPERTY_SEPARATOR +
                    TextStyles.dim(value)

            terminal.println(Text(property))
        }
    }

    private fun showAllEnvironments() {

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

        val target = runCatching {
            Target(config.targetPath)
        }.getOrElse {
            return environment.nameWithoutExtension
        }

        if (environment.readAsMap() == target.read().toMap()) {
            return environment.nameWithoutExtension
        }

        return environment.nameWithoutExtension + "*"
    }
}