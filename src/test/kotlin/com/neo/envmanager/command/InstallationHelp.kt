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

        paths.installationDir.mkdir()

        targetFile.createNewFile()

        paths.configFile.writeText(
            Gson().toJson(
                Config(
                    targetPath = targetFile.path,
                )
            )
        )
    }
}