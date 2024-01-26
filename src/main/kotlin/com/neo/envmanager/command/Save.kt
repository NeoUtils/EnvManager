package com.neo.envmanager.command

import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.google.gson.Gson
import com.neo.envmanager.com.neo.envmanager.exception.error.NotSupportedTransferData
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.Cancel
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.extension.*
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

class Save : Command(help = "Save current environment") {

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

        val environment = paths.environmentsDir.apply {
            if (!exists()) mkdirs()
        }.resolve(tag.json)

        if (environment.exists() && this.tag != null) {

            echo("! Environment $tag already exists")

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        environment
            .writeText(
                Gson().toJson(
                    properties.toMap()
                )
            )

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

        paths.configFile.writeText(
            Gson().toJson(
                config.copy(
                    currentEnv = tag
                )
            )
        )

        updateTarget(tag)
    }

    private fun updateTarget(tag: String) {

        val target = File(config.targetPath)

        val environment = paths.environmentsDir.resolve(tag.json)

        target.writeText(
            environment
                .readAsMap()
                .entries
                .joinToString(separator = "\n")
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

        val target = File(config.targetPath)

        if (!target.exists()) {
            throw TargetNotFound(target.path)
        }

        return target.readAsProperties()
    }
}