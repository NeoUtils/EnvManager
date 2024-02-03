package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class KeyNotFound(
    key: String,
    environment: String? = null,
    theme: Theme = Theme.Default
) : CliktError(
    theme.danger(
        text = environment?.let {
            "✖ Key '$key' not found in '$environment'"
        } ?:  "✖ Key '$key' not found"
    )
)
