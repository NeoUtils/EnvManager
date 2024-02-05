package com.neo.envmanager.model

import com.neo.envmanager.util.extension.readAsConfig
import java.io.File

data class Installation(
    val config: Config,
    val environmentsDir: File
) {
    constructor(paths: Paths) : this(
        config = paths.configFile.readAsConfig(),
        environmentsDir = paths.environmentsDir
    )
}