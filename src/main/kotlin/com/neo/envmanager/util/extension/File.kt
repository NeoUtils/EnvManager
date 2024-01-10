package com.neo.envmanager.util.extension

import com.google.gson.Gson
import com.neo.envmanager.util.MapTypeToken
import com.neo.envmanager.model.Config
import java.io.File
import java.util.*

fun File.readAsProperties(): Properties {

    return Properties().apply {
        load(inputStream())
    }
}

fun File.readAsConfig(): Config {

    return Gson().fromJson(
        readText(),
        Config::class.java
    )
}

fun File.readAsMap(): Map<String, String> {

    return Gson().fromJson(
        readText(),
        MapTypeToken.type
    )
}