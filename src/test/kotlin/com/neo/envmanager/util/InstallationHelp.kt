package com.neo.envmanager.util

import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Paths
import java.io.File

data class InstallationHelp(
    val projectDir: File = File("build/tmp/test"),
    val targetFile: File = File(projectDir, "test.properties"),
    val paths: Paths = Paths(projectDir)
) {

    val installed get() = paths.configFile.exists()
    val ready get() = targetFile.exists()

    fun setup() = apply {

        if (ready) return@apply

        targetFile.createNewFile()
    }

    fun clear() = apply{
        paths.installationDir.deleteRecursively()
        targetFile.delete()
    }

    fun install() {

        if (installed) return

        setup()

        paths.installationDir.mkdir()

        Config(
            targetFile = targetFile
        ).writeTo(paths.configFile)
    }

    fun updateConfig(block: (Config) -> Config) {
        block(
            Config.loadFrom(paths.configFile)
        ).writeTo(paths.configFile)
    }
}

fun installed(
    installation: InstallationHelp = InstallationHelp(),
    block: InstallationHelp.() -> Unit
) {

    installation.clear()
    installation.install()

    block(installation)

    installation.clear()
}