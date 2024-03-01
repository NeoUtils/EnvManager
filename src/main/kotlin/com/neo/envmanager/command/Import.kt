package com.neo.envmanager.command

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.FilePromise
import com.neo.envmanager.util.Constants
import com.neo.envmanager.util.MapTypeToken
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.gson

class Import : CliktCommand(
    help = "Import environments"
) {

    private val file by argument(
        help = "Environment(s) file (${Constants.DOT_ENVM})"
    ).file(
        mustExist = true,
        canBeDir = false,
        canBeFile = true
    ).validate { file ->
        if (!file.name.endsWith(Constants.DOT_ENVM)) {
            fail("Invalid extension '${file.extension}'")
        }
    }

    override fun run() {

        val installation = requireInstall()

        val type = MapTypeToken<MapTypeToken<String>>().type

        val environments = gson.fromJson<Map<String, Map<String, String>>>(file.readText(), type)

        environments.forEach { (tag, map) ->

            val promise = FilePromise(installation.environmentsDir, tag)

            if (promise.exists()) {

                echo(message = "Environment '$tag' already exists.")

                val mustOverwriteMessage = "Do you want to overwrite it?"

                if (YesNoPrompt(mustOverwriteMessage, terminal).ask() != true) throw Abort()
            }

            Environment.getOrCreate(promise).write(map)
        }
    }
}