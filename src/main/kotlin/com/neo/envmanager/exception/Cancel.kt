package com.neo.envmanager.exception

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme
import com.neo.envmanager.com.neo.envmanager.model.Platform
import com.neo.envmanager.util.extension.dangerSymbol

class Cancel(
    theme: Theme = Theme.Default
) : CliktError(
    message = theme.dangerSymbol(text="Canceled"),
    printError = false
)