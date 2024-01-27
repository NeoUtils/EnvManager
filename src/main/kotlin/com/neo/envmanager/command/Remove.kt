package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.KeyNotFound
import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

class Remove : Command(
    help = "Remove one or more properties"
) {

    private val keys by argument(
        help = "Properties tags to delete, separated by space"
    ).multiple()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        removeInTarget()
    }

    private fun removeInTarget() {

        val target = File(config.targetPath)

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        val properties = target.readAsProperties()

        for (key in keys) {

            if (!properties.containsKey(key)) {
                echoFormattedHelp(KeyNotFound(key))
                continue
            }

            properties.remove(key)
        }

        target.writeText(
            properties
                .entries
                .joinToString(separator = "\n")
        )
    }
}