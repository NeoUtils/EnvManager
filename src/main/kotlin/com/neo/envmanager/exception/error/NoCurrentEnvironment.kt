package com.neo.envmanager.com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class NoCurrentEnvironment(
    theme: Theme = Theme.Default
) : CliktError(
    message = theme.danger(text = "✖ No current environment"),
)