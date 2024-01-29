package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.com.neo.envmanager.exception.error.NotSupportedTransferData
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import com.neo.envmanager.util.extension.update
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

class Save : Command(help = "Save target to an environment") {

    private val tag by tag().optional()

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

        val environmentFile = paths.environmentsDir.resolve(tag.json)

        if (environmentFile.exists() && this.tag != null) {

            echo(message = "! Environment $tag already exists")

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        Environment
            .getOrCreate(paths.environmentsDir, tag)
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

        val environment = Environment.get(paths.environmentsDir, tag)

        target.write(
            environment
                .read()
                .toProperties()
        )
    }

    private fun getProperties(): Properties {

        if (fromClipboard) {

            // Don't print exception https://youtrack.jetbrains.com/issue/IDEA-324810
            System.setErr(PrintStream(OutputStream.nullOutputStream()))

            val clipboard = Toolkit.getDefaultToolkit().systemClipboard

            val contents = clipboard.getContents(DataFlavor.stringFlavor)

            if (!contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                throw NotSupportedTransferData()
            }

            val data = contents.getTransferData(DataFlavor.stringFlavor)

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