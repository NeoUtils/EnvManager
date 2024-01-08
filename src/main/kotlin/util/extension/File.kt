package com.neo.properties.util.extension

import com.google.gson.Gson
import com.neo.properties.model.Config
import com.neo.properties.util.MapTypeToken
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