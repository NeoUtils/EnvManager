package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class NotInstalledError(
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(text = "✕ Not installed")
)