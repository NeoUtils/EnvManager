package com.neo.properties.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.neo.properties.util.Constants
import java.io.File

abstract class Command(
    name: String? = null,
    help: String
) : CliktCommand(name = name, help = help) {

    protected val projectDir by requireObject<File>()

    protected val installDir by lazy {
        projectDir.resolve(Constants.INSTALL_FOLDER_PATH)
    }

    // TODO: improve this
    val configFile by lazy {
        installDir.resolve(Constants.CONFIG_FILE_PATH)
    }

    protected val environmentsDir by lazy {
        installDir.resolve(Constants.ENVIRONMENTS_FOLDER_PATH)
    }

    fun isInstalled() = configFile.exists()
}