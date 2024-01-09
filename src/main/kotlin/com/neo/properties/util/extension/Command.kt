package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.Abort
import com.neo.properties.core.Command
import com.neo.properties.error.NotInstalledError
import com.neo.properties.model.Config
import com.neo.properties.model.Paths
import com.neo.properties.util.Instructions

fun Command.requireInstall(): Config {

    val paths = checkNotNull(currentContext.findObject<Paths>())

    if (!paths.isInstalled()) {

        echoFormattedHelp(NotInstalledError())

        echo(Instructions.INSTALL)

        throw Abort()
    }

    return paths.configFile.readAsConfig()
}