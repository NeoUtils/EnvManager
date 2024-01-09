package com.neo.properties.commands

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.properties.core.Command
import com.neo.properties.error.TargetNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.readAsProperties
import com.neo.properties.util.extension.requireInstall

/**
 * Save current environment command
 * @author Irineu A. Silva
 */
class Save : Command(help = "Save current environment") {

    private val tag by argument(
        help = "Tag to save"
    )

    override fun run() {

        val (target, environments) = requireInstall().withFile()

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        if (!environments.exists()) environments.mkdirs()

        val environment = environments.resolve(tag.json)

        if (environment.exists()) {

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) {

                echo("✖ Aborted")

                throw Abort()
            }
        }

        environment
            .writeText(
                Gson().toJson(
                    target.readAsProperties().toMap()
                )
            )
    }
}