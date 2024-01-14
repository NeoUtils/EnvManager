package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme
import com.neo.envmanager.util.extension.dangerSymbol

class NoEnvironmentsFound(
    theme: Theme = Theme.Default
) : CliktError(
    message = theme.dangerSymbol(
        text = "No environments found"
    )
)