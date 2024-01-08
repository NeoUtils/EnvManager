package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.Abort
import com.neo.properties.core.BaseCommand
import com.neo.properties.util.Instructions
import com.neo.properties.model.Config
import com.neo.properties.util.Constants
import errors.NotInstalledError

fun BaseCommand.requireInstall(): Config {

    val configFile = path.resolve(Constants.CONFIG_FILE_PATH)

    if (!configFile.exists()) {

        echoFormattedHelp(NotInstalledError())

        echo(Instructions.INSTALL)

        throw Abort()
    }

    return configFile.readAsConfig()
}