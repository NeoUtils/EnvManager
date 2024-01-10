package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.TargetNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

/**
 * Save current environment command
 * @author Irineu A. Silva
 */
class Save : Command(help = "Save current environment") {

    private val tag by argument(
        help = "Tag to save"
    )

    override fun run() {

        val target = File(requireInstall().targetPath)

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        val environmentsDir = paths.environmentsDir

        if (!environmentsDir.exists()) environmentsDir.mkdirs()

        val environment = environmentsDir.resolve(tag.json)

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