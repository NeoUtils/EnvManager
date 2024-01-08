package com.neo.properties.commands

import com.github.ajalt.clikt.parameters.arguments.argument
import com.google.gson.Gson
import com.neo.properties.core.BaseCommand
import com.neo.properties.errors.TargetNotFound
import com.neo.properties.util.extension.json
import com.neo.properties.util.extension.readAsProperties
import com.neo.properties.util.extension.requireInstall

/**
 * Save current environment command
 * @author Irineu A. Silva
 */
class Save : BaseCommand(help = "Save current environment") {

    private val tag by argument(
        help = "Tag to save"
    )

    override fun run() {

        val (target, environments) = requireInstall().withFile()

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        if (!environments.exists()) environments.mkdirs()

        environments
            .resolve(tag.json)
            .writeText(
                Gson().toJson(
                    target.readAsProperties().toMap()
                )
            )

        echo("✔ Properties saved with tag '$tag'")
    }
}