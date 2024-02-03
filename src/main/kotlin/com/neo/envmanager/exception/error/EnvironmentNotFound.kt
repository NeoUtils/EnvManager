package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class EnvironmentNotFound(
    tag: String,
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(text = "✖ Environment '$tag' not found")
)
