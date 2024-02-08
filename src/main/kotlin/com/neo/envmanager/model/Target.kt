package com.neo.envmanager.model

import Resource
import com.neo.envmanager.exception.error.TargetNotFound
import com.neo.envmanager.util.extension.iterable
import java.io.File
import java.util.*

@JvmInline
value class Target(val file: File) {

    constructor(path: String) : this(File(path))

    init {
        if (!file.exists()) throw TargetNotFound(file.path)
    }

    fun write(properties: Properties) {
        file.writeText(
            properties.iterable().joinToString(
                separator = "\n"
            ) { (key, value) ->
                "$key=$value"
            }
        )
    }

    fun read() = Properties().apply {
        load(file.inputStream())
    }

    fun add(properties: Properties) {
        write(read().apply { putAll(properties) })
    }

    companion object {

        fun getSafe(file: File) = try {
            Resource.Result.Success(Target(file))
        } catch (e: TargetNotFound) {
            Resource.Result.Failure(e)
        }

        fun getOrCreate(promise: FilePromise): Target {

            val file = promise.file

            if (!file.exists()) file.createNewFile()

            return Target(file)
        }
    }
}