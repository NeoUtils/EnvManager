package com.neo.envmanager.model

import java.io.File

data class Installation(
    val config: Config,
    val environmentsDir: File
) {
    constructor(paths: Paths) : this(
        config = Config.loadFrom(paths.configFile),
        environmentsDir = paths.environmentsDir
    )
}