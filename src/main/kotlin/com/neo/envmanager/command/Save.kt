package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.io.File

/**
 * Save current environment command
 * @author Irineu A. Silva
 */
class Save : Command(help = "Save current environment") {

    private val tag by tag().optional()

    override fun run() {

        val config = requireInstall()

        val target = File(config.targetPath)

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        val environmentsDir = paths.environmentsDir

        if (!environmentsDir.exists()) environmentsDir.mkdirs()

        val environment = environmentsDir.resolve(tag.json)

        if (environment.exists()) {

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        environment
            .writeText(
                Gson().toJson(
                    target.readAsProperties().toMap()
                )
            )
    }
}