package com.neo.envmanager.model

import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.util.extension.iterable
import java.io.File

@JvmInline
value class Target(val file: File) {

    constructor(path: String) : this(File(path))

    init {
        if (!file.exists()) throw TargetNotFound(file.path)
    }

    fun write(properties: Map<*, *>) {
        file.writeText(
            properties.iterable().joinToString(
                separator = "\n"
            ) { (key, value) ->
                "$key=$value"
            }
        )
    }
}