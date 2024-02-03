package com.neo.envmanager.exception

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class Cancel(
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(text = "✖ Canceled"),
    printError = false
)