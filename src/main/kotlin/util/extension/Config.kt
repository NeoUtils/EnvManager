package com.neo.properties.util.extension

import com.github.ajalt.clikt.core.CliktCommand
import com.google.gson.Gson
import com.neo.properties.model.Config
import com.neo.properties.util.Constants
import java.io.File

context(CliktCommand)
fun Config.save(
    installation: File,
) {
    installation.resolve(
        Constants.CONFIG_PATH
    ).writeText(Gson().toJson(this))


}