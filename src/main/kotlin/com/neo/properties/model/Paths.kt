package com.neo.properties.model

import com.neo.properties.util.Constants
import java.io.File

class Paths(
    val projectDir: File
) {

    val installationDir = projectDir.resolve(Constants.INSTALL_FOLDER_PATH)
    val configFile = installationDir.resolve(Constants.CONFIG_FILE_PATH)
    val environmentsDir = installationDir.resolve(Constants.ENVIRONMENTS_FOLDER_PATH)

    fun isInstalled() = configFile.exists()
}