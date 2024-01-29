package com.neo.envmanager.com.neo.envmanager.util.extension

import com.github.ajalt.clikt.core.CliktCommand
import com.google.gson.Gson
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Paths

context(CliktCommand)
fun Config.update(block: (Config) -> Config = { it }): Config {

    val paths = checkNotNull(currentContext.findObject<Paths>())

    return block(this).also {
        paths.configFile.writeText(
            Gson().toJson(it)
        )
    }
}