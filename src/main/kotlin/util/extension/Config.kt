package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.CliktCommand
import com.google.gson.Gson
import com.neo.properties.model.Config
import com.neo.properties.util.Constants
import java.io.File

context(CliktCommand)
fun Config.create(
    installation: File,
) {
    installation.resolve(
        Constants.CONFIG_FILE
    ).writeText(Gson().toJson(this))

    echo("✔ Config file created")

    installation.tryAddToGitIgnore(Constants.CONFIG_FILE)
}