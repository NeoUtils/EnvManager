package com.neo.envmanager.model

import Resource
import com.google.gson.Gson
import com.neo.envmanager.com.neo.envmanager.exception.error.EnvironmentAlreadyExists
import com.neo.envmanager.exception.error.EnvironmentNotFound
import com.neo.envmanager.util.MapTypeToken
import com.neo.envmanager.util.extension.json
import java.io.File

@JvmInline
value class Environment(val file: File) {

    constructor(path: String) : this(File(path))
    constructor(dir: File, tag: String) : this(dir.resolve(tag.json))

    init {
        if (!file.exists()) throw EnvironmentNotFound(file.nameWithoutExtension)
    }

    val tag get() = file.nameWithoutExtension

    fun read(): Map<String, String> {

        return runCatching<Map<String, String>> {
            Gson().fromJson(
                file.readText(),
                MapTypeToken.type
            )
        }.getOrElse {
            emptyMap()
        }
    }

    fun write(properties: Map<*, *>) {
        file.writeText(
            Gson().toJson(properties)
        )
    }

    fun add(properties: Map<*, *>) {
        write(properties = read() + properties)
    }

    fun renameTo(tag: String): Environment {

        val newFile = file.parentFile.resolve(tag.json)

        if (newFile.exists()) throw EnvironmentAlreadyExists(tag)

        file.renameTo(newFile)

        return Environment(newFile)
    }

    companion object {

        fun getSafe(file: File) = try {
            Resource.Result.Success(Environment(file))
        } catch (e: EnvironmentNotFound) {
            Resource.Result.Failure(e)
        }

        fun getSafe(dir: File, tag: String) =  getSafe(dir.resolve(tag.json))

        fun getOrCreate(dir: File, tag: String) = getOrCreate(FilePromise(dir, tag))

        fun getOrCreate(promise: FilePromise): Environment {

            val file = promise.file

            val environments = file.parentFile

            if (!environments.exists()) environments.mkdir()

            if (!file.exists()) file.createNewFile()

            return Environment(file)
        }
    }
}
