package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.core.Command
import com.neo.envmanager.util.extension.properties

class Setter : Command(
    name = "set",
    help = "Set properties to environment",
) {

    private val properties by argument(
        help = "Properties to set, separated by space",
        helpTags = mapOf("KEY=VALUE" to "Property"),
    ).properties(required = true)

    override fun run() {
        println(properties)
    }
}



