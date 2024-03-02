package com.neo.envmanager.util.extension

import com.neo.envmanager.model.Config
import com.neo.envmanager.util.Constants
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

fun File.jsonFiles(): Array<out File> {

    return listFiles { _, name ->
        name.endsWith(Constants.DOT_JSON)
    }.orEmpty()
}

fun File.resolveCollision(): File {

    var file = this
    var index = 0

    val name = file.nameWithoutExtension
    val extension = file.extension

    while (file.exists()) {
        file = File(file.parent, "$name(${++index}).$extension")
    }

    return file
}
