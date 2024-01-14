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
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsProperties
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.io.File
import java.util.*

class Save : Command(help = "Save current environment") {

    private val tag by tag().optional()

    private val clipboard by option(
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

        if (environment.exists()) {

            // Show warning only when specifying environment
            if (this.tag != null) echo("! Environment $tag already exists")

            val overwritePrompt = YesNoPrompt(prompt = "Overwrite $tag?", terminal)

            if (overwritePrompt.ask() != true) throw Cancel()
        }

        environment
            .writeText(
                Gson().toJson(
                    properties.toMap()
                )
            )
    }

    private fun getProperties(): Properties {

        if (clipboard) {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard

            val contents = clipboard.getContents(null)

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