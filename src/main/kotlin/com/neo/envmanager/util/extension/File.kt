package com.neo.envmanager.com.neo.envmanager.util.extension

import com.neo.envmanager.model.Environment
import com.neo.envmanager.util.Constants
import java.io.File

fun File.jsonFiles(): Array<out File> {

    return listFiles { _, name ->
        name.endsWith(Constants.DOT_JSON)
    }.orEmpty()
}

fun File.getEnvironments() = jsonFiles().map(::Environment)