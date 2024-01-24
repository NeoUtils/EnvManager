package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.core.Command
import com.neo.envmanager.util.extension.properties
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import java.io.File

class Setter : Command(
    name = "set",
    help = "Set properties to environment",
) {
    private val properties by argument(
        help = "Properties to set, separated by space",
        helpTags = mapOf("KEY=VALUE" to "Property"),
    ).properties(required = true)

    override fun run() {

        val target = File(requireInstall().targetPath)

        val updatedProperties = target.readAsProperties() + properties

        target.writeText(
            updatedProperties
                .entries
                .joinToString(separator = "\n")
        )
    }
}



