package com.neo.properties.model

import java.io.File as JavaFile

data class Config(
    val targetPath: String,
    val environmentsPath: String
) {
    fun withFile(): File {

        return File(
            targetPath = JavaFile(targetPath),
            environmentsPath = JavaFile(environmentsPath)
        )
    }

    data class File(
        val targetPath: JavaFile,
        val environmentsPath: JavaFile
    )
}