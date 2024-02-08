package com.neo.envmanager.model

import com.google.gson.Gson
import com.neo.envmanager.util.MapTypeToken
import java.io.File

data class Config(
    val targetFile: File,
    val currentEnv: String? = null,
) {

    constructor(map: Map<String, Any>) : this(
        targetFile = File(map[TARGET_PATH] as String),
        currentEnv = map[CURRENT_ENVIRONMENT] as String?
    )

    fun writeTo(file: File) = file.writeText(
        Gson().toJson(
            mapOf(
                TARGET_PATH to targetFile.path,
                CURRENT_ENVIRONMENT to currentEnv
            )
        )
    )

    companion object {

        private const val TARGET_PATH = "target_path"
        private const val CURRENT_ENVIRONMENT = "current_environment"

        fun loadFrom(file: File): Config {

            val properties = runCatching<Map<String, String>> {
                Gson().fromJson(
                    file.readText(),
                    MapTypeToken.type
                )
            }.getOrElse {
                emptyMap()
            }

            return Config(properties)
        }
    }
}