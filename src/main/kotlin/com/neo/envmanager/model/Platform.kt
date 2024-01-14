package com.neo.envmanager.com.neo.envmanager.model

enum class Platform {
    LINUX,
    WINDOWS,
    OTHER;

    companion object {

        operator fun invoke(): Platform {

            val osName = System.getProperty("os.name").lowercase()

            return when {
                "linux" in osName -> LINUX
                "windows" in osName -> WINDOWS
                else -> OTHER
            }
        }
    }
}