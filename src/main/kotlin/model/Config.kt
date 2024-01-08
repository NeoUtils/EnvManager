package com.neo.properties.model

import java.io.File as JavaFile

data class Config(
    val targetPath: String,
    val environmentsPath: String
) {
    fun withFile(): File {

        return File(
            target = JavaFile(targetPath),
            environments = JavaFile(environmentsPath)
        )
    }

    data class File(
        val target: JavaFile,
        val environments: JavaFile
    )
}