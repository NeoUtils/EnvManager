package com.neo.envmanager.model

import Resource
import com.google.gson.Gson
import com.neo.envmanager.com.neo.envmanager.exception.error.EnvironmentAlreadyExists
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.extension.json
import com.neo.envmanager.util.extension.readAsMap
import java.io.File

@JvmInline
value class Environment(val file: File) {

    constructor(path: String) : this(File(path))
    constructor(dir: File, tag: String) : this(dir.resolve(tag.json))

    init {
        if (!file.exists()) throw EnvironmentNotFound(file.nameWithoutExtension)
    }

    fun read(): Map<String, String> {
        return file.readAsMap()
    }

    fun write(properties: Map<*, *>) {
        file.writeText(
            Gson().toJson(properties)
        )
    }

    fun renameTo(tag: String) : Environment {

        val newFile = file.parentFile.resolve(tag.json)

        if (newFile.exists()) throw EnvironmentAlreadyExists(tag)

        file.renameTo(newFile)

        return Environment(newFile)
    }

    fun add(properties:  Map<*, *>) {
        write(read() + properties)
    }

    companion object {

        fun getSafe(dir: File, tag: String) = try {
            Resource.Result.Success(Environment(dir.resolve(tag.json)))
        } catch (e: EnvironmentNotFound) {
            Resource.Result.Failure(e)
        }

        fun getOrCreate(dir: File, tag: String): Environment {

            if (!dir.exists()) { dir.mkdir() }

            val file = dir.resolve(tag.json)

            if (!file.exists()) { file.createNewFile() }

            return Environment(file)
        }
    }
}
