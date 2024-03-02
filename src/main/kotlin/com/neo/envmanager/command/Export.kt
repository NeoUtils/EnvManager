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
import com.neo.envmanager.com.neo.envmanager.util.NotBlankValidation
import com.neo.envmanager.com.neo.envmanager.util.extension.environments
import com.neo.envmanager.exception.error.NoEnvironmentsFound
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Installation
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.envm
import com.neo.envmanager.util.extension.promptFile
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.resolveCollision
import com.neo.envmanager.util.gson
import java.io.File

class Export : CliktCommand(
    help = "Export environments"
) {

    private val output by option(
        names = arrayOf("--output"),
        help = "Output directory"
    ).file(
        canBeDir = true,
        canBeFile = false
    ).defaultLazy {
        terminal.promptFile(
            text = "Output directory",
            canBeDir = true,
            canBeFile = false,
            default = File("output")
        )
    }

    private val tags by argument(
        help = "Specific tag (optional"
    ).multiple()

    private lateinit var installation: Installation

    override fun run() {

        installation = requireInstall()

        if (!output.exists()) {

            echo("Output directory '${output.path}' does not exist.")

            if (YesNoPrompt("Create it?", terminal).ask() != true) throw Abort()

            output.mkdirs()
        }

        if (tags.isEmpty()) {
            exportAllEnvironments()
            return
        }

        exportSpecificEnvironment()
    }

    private fun exportAllEnvironments() {

        val environments = installation.environments()

        if (environments.isEmpty()) {
            throw NoEnvironmentsFound()
        }

        writeExportFile(environments)
    }

    private fun exportSpecificEnvironment() {

        val environments = tags.map {
            Environment(installation.environmentsDir, it)
        }

        writeExportFile(environments)
    }

    private fun writeExportFile(
        environments: List<Environment>
    ) {
        val name = when {
            environments.size == 1 -> {
                environments.single().tag
            }

            tags.isEmpty() -> {
                Target(installation.config.targetPath).name
            }

            else -> {
                checkNotNull(
                    terminal.prompt(
                        prompt = "Choose a name for the export file",
                        convert = NotBlankValidation
                    )
                )
            }
        }

        output.resolve(name.envm).resolveCollision().writeText(
            gson.toJson(
                buildMap {
                    environments.forEach {
                        put(it.tag, it.read())
                    }
                }
            )
        )
    }
}

