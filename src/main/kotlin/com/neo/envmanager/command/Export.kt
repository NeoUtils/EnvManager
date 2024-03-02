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
            canBeFile = false,
            default = File("output")
        )
    }

    private val tag by argument(
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

        if (tag.isEmpty()) {
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

        val environments = tag.map {
            Environment(installation.environmentsDir, it)
        }

        writeExportFile(environments)
    }

    private fun writeExportFile(
        environments: List<Environment>
    ) {

        val default by lazy {
            Target(installation.config.targetPath)
        }

        val name = checkNotNull(
            terminal.prompt(
                prompt = "Choose a name for the export file",
                default = environments.singleOrNull()?.tag ?: default.name,
                convert = NotBlankValidation
            )
        )

        output.resolve(name.envm).writeText(
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