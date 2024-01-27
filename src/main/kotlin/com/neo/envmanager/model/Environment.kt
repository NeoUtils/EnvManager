package com.neo.envmanager.model

import com.google.gson.Gson
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import java.io.File

@JvmInline
value class Environment(val file: File) {

    constructor(path: String) : this(File(path))

    init {
        if (!file.exists()) throw EnvironmentNotFound(file.path)
    }

    fun read(): Map<String, String> {
        return file.readAsMap()
    }

    fun write(properties: Map<String, String>) {
        file.writeText(
            Gson().toJson(properties)
        )
    }

    companion object {

        fun fromTag(dir: File, tag: String): Environment {

            return Environment(dir.resolve(tag.json))
        }
    }
}
