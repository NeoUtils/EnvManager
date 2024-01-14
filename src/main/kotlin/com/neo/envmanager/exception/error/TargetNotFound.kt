package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme
import com.neo.envmanager.com.neo.envmanager.model.Platform
import com.neo.envmanager.util.extension.dangerSymbol

class TargetNotFound(
    path: String,
    theme: Theme = Theme.Default
) : CliktError(
    message = theme.dangerSymbol(
        text = "Environment properties '$path' not found"
    )
)