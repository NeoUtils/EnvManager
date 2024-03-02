package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.extension.promptFile
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.gson
import java.io.File

class Export : CliktCommand(
    help = "Export environments"
) {

    private val output by option(
        names = arrayOf("-o", "--output"),
        help = "Output directory"
    ).file(
        canBeDir = true,
        canBeFile = false
    ).defaultLazy {
        terminal.promptFile(
            text = "Output directory",
            canBeDir = true,
            canBeFile = false
        )
    }

    private val tag by argument(
        help = "Specific tag (optional"
    ).multiple()

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        if (!output.exists()) {

            val mustCreateMessage = "Output directory '${output.path}' does not exist. Create it?"

            if (YesNoPrompt(mustCreateMessage, terminal).ask() != true) throw Abort()

            output.mkdirs()
        }

        if (tag.isEmpty()) {
            exportAllEnvironments()
            return
        }

        exportSpecificEnvironment()
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

    private fun exportSpecificEnvironment() {

        val environments = tag.map { Environment(installation.environmentsDir, it) }

        val name = environments.singleOrNull()?.tag ?: Target(installation.config.targetPath).name

        getExportFile(name).writeText(
            gson.toJson(
                buildMap {
                    environments.forEach {
                        put(it.tag, it.read())
                    }
                }
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

            file = output.resolve(filePath)

            count++

        } while (file.exists())

        return file
    }
}