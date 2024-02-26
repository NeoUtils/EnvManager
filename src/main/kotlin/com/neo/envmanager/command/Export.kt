package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.gson
import java.io.File

class Export : CliktCommand(
    help = "Export all environments"
) {

    private val destinationDir by argument(
        name = "destination",
        help = "Destination directory"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    )

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        val environments = installation.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }?.map {
            Environment(it)
        }

        if (environments.isNullOrEmpty()) {
            throw NoEnvironmentsFound()
        }

        getExportFile().writeText(
            gson.toJson(
                buildMap {
                    environments.forEach {
                        put(it.tag, it.read())
                    }
                }
            )
        )
    }

    private fun getExportFile(): File {

        var file: File
        var count = 0

        val target = Target(
            installation.config.targetPath
        )

        do {

            val filePath = buildString {

                append(target.name)

                if (count > 0) {
                    append("($count)")
                }

                append(Constants.DOT_ENVM)
            }

            file = destinationDir.resolve(filePath)

            count++

        } while (file.exists())

        return file
    }
}