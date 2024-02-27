package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
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
    help = "Export environments"
) {

    private val destinationDir by argument(
        name = "destination",
        help = "Destination directory"
    ).file(
        mustExist = true,
        canBeDir = true,
        canBeFile = false
    )

    private val tag by option(
        help = "Specific tag (optional"
    )

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        if (tag != null) {
            exportSpecificEnvironment(tag!!)
            return
        }

        exportAllEnvironments()
    }

    private fun exportAllEnvironments() {

        val environments = installation.environmentsDir.listFiles { _, name ->
            name.endsWith(Constants.DOT_JSON)
        }?.map {
            Environment(it)
        }

        if (environments.isNullOrEmpty()) {
            throw NoEnvironmentsFound()
        }

        val target = Target(installation.config.targetPath)

        getExportFile(target.name).writeText(
            gson.toJson(
                buildMap {
                    environments.forEach {
                        put(it.tag, it.read())
                    }
                }
            )
        )
    }

    private fun exportSpecificEnvironment(tag: String) {

        val environment = Environment(installation.environmentsDir, tag)

        getExportFile(tag).writeText(
            gson.toJson(
                mapOf(environment.tag to environment.read())
            )
        )
    }

    private fun getExportFile(name: String): File {

        var file: File
        var count = 0

        do {

            val filePath = buildString {

                append(name)

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