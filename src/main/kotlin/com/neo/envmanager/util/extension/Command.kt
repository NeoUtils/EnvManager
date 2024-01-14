package com.neo.envmanager.util.extension

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.Theme
import com.neo.envmanager.com.neo.envmanager.model.Platform
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.NotInstalledError
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Paths
import com.neo.envmanager.util.Instructions

fun Command.requireInstall(): Config {

    val paths = checkNotNull(currentContext.findObject<Paths>())

    if (!paths.isInstalled()) {

        echoFormattedHelp(NotInstalledError())

        echo(Instructions.INSTALL)

        throw Abort()
    }

    return paths.configFile.readAsConfig()
}

fun Command.tag() = argument(
    name = "tag",
    help = "Environment tag"
)

fun Theme.successSymbol(
    text: String
) = success(
    text = when(Platform()) {
        Platform.LINUX -> "✓ $text"
        else -> text
    }
)

fun Theme.dangerSymbol(
    text: String
) = danger(
    text = when(Platform()) {
        Platform.LINUX -> "✕ $text"
        else -> text
    }
)
