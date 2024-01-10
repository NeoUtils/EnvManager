package com.neo.envmanager.util.extension

import com.github.ajalt.clikt.core.Abort
import com.neo.envmanager.core.Command
import com.neo.envmanager.error.NotInstalledError
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