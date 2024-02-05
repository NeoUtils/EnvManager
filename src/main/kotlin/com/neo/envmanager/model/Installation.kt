package com.neo.envmanager.model

import java.io.File

data class Installation(
    val config: Config,
    val environmentsDir: File
)