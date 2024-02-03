package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.com.neo.envmanager.util.mutedErrorOutput
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.NotSupportedTransferData
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.FilePromise
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.util.*

class Save : Command(help = "Save target to an environment") {

    private val tag by tag().optional()

    // TODO: migrate to set command
    private val fromClipboard by option(
        names = arrayOf("-c", "--clipboard"),
        help = "Save from clipboard"
    ).flag()

    private lateinit var config: Config

    override fun run() {

        config = requireInstall()

        // Get tag from current environment or from argument
        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val properties = getProperties()

        val promise = FilePromise(paths.environmentsDir, tag)

        if (promise.file.exists() && this.tag != null) {

            echo(message = "! Environment $tag already exists")

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        Environment
            .getOrCreate(promise)
            .write(properties.toMap())

        // Update target when current environment is modified
        if (fromClipboard && tag == config.currentEnv) {
            updateTarget(tag)
            return
        }

        // Checkout when save target without environment
        if (!fromClipboard && config.currentEnv == null) {
            checkout(tag)
        }
    }

    private fun checkout(tag: String) {

        config.update {
            it.copy(
                currentEnv = tag
            )
        }

        updateTarget(tag)
    }

    private fun updateTarget(tag: String) {

        val target = Target(config.targetPath)

        val environment = Environment(paths.environmentsDir, tag)

        target.write(
            environment
                .read()
                .toProperties()
        )
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

        return Target(config.targetPath).read()
    }
}