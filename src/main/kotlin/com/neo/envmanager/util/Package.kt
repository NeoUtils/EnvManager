package com.neo.envmanager.util

object Package {

    val version: String
        get() {
            val `package` = javaClass.`package`

            return `package`.implementationVersion ?: "DEBUG MODE"
        }
}