package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.neo.properties.core.Command
import com.neo.properties.util.Instructions
import com.neo.properties.model.Config
import com.neo.properties.util.Constants
import errors.NotInstalledError
import java.io.File

fun Command.requireInstall(): Config {

    val pathDir = checkNotNull(currentContext.findObject<File>())

    val configFile = pathDir.resolve(Constants.CONFIG_FILE_PATH)

    if (!configFile.exists()) {

        echoFormattedHelp(NotInstalledError())

        echo(Instructions.INSTALL)

        throw Abort()
    }

    return configFile.readAsConfig()
}