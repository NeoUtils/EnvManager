package com.neo.envmanager.util.extension

import com.neo.envmanager.model.Config
import com.neo.envmanager.util.MapTypeToken
import com.neo.envmanager.util.gson
import java.io.File

fun File.readAsConfig(): Config {

    return gson.fromJson(
        readText(),
        Config::class.java
    )
}

fun File.readAsMap(): Map<String, String> {

    val type = MapTypeToken<String>().type

    return runCatching<Map<String, String>> {
        gson.fromJson(
            readText(),
            type
        )
    }.getOrElse {
        emptyMap()
    }
}
