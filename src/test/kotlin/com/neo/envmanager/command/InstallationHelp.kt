package com.neo.envmanager.command

import com.google.gson.Gson
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Paths
import java.io.File

class InstallationHelp(
    val projectDir: File = File("build/tmp/test"),
    val targetFile: File = File(projectDir, "test.properties"),
    val paths: Paths = Paths(projectDir)
) {

    private val installed get() = paths.configFile.exists()
    private val ready get() = targetFile.exists()

    fun setup() {

        if (ready) return

        targetFile.createNewFile()
    }

    fun clear() {
        paths.installationDir.deleteRecursively()
        targetFile.delete()
    }

    fun install() {

        if (installed) return

        setup()

        paths.installationDir.mkdir()

        paths.configFile.writeText(
            Gson().toJson(
                Config(
                    targetPath = targetFile.path
                )
            )
        )
    }

    fun updateConfig(block: (Config) -> Config) {

        val config = paths.configFile.readText().let {
            Gson().fromJson(it, Config::class.java)
        }

        paths.configFile.writeText(
            Gson().toJson(block(config))
        )
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