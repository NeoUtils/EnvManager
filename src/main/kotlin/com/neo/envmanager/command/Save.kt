package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.com.neo.envmanager.util.mutedErrorOutput
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.NotSupportedTransferData
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.*
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.util.*

class Save : CliktCommand(help = "Save target to an environment") {

    private val tag by tag().optional()

    // TODO: migrate to set command
    private val fromClipboard by option(
        names = arrayOf("-c", "--clipboard"),
        help = "Save from clipboard"
    ).flag()

    private lateinit var installation: Installation

    override fun run() {

        val (config, environmentsDir) = requireInstall().also { installation = it }

        // Get tag from current environment or from argument
        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val properties = getProperties()

        val promise = FilePromise(environmentsDir, tag)

        if (promise.file.exists() && this.tag != null) {

            echo(message = "! Environment $tag already exists")

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        val environment = Environment.getOrCreate(promise)

        environment.write(properties.toMap())

        // Update target when current environment is modified
        if (fromClipboard && tag == config.currentEnv) {
            environment.updateTarget()
            return
        }

        // Checkout when save target without environment
        if (!fromClipboard && config.currentEnv == null) {
            environment.checkout()
        }
    }

    private fun Environment.checkout() {

        val tag = file.nameWithoutExtension

        installation.config.update {
            it.copy(
                currentEnv = tag
            )
        }

        updateTarget()
    }

    private fun Environment.updateTarget() {

        val target = Target(installation.config.targetFile)

        target.write(read().toProperties())
    }

    private fun getProperties(): Properties {

        if (fromClipboard) {

            val data = mutedErrorOutput {

                val clipboard = Toolkit.getDefaultToolkit().systemClipboard

                val contents = clipboard.getContents(null)

                if (!contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    throw NotSupportedTransferData()
                }

                contents.getTransferData(DataFlavor.stringFlavor)
            }

            return Properties().apply {
                load(
                    ByteArrayInputStream(
                        data.toString().toByteArray()
                    )
                )
            }
        }

        return Target(installation.config.targetFile).read()
    }
}