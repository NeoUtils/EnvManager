package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class KeyNotFound(
    key: String,
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(text = "✖ Key '$key' not found")
)
