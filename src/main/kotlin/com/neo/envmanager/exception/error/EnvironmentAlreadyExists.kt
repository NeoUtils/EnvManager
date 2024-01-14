package com.neo.envmanager.com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class EnvironmentAlreadyExists(
    tag: String,
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(text = "✖ Environment '$tag' already exists")
)