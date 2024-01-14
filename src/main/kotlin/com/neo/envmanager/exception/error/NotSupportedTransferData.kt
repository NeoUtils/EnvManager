package com.neo.envmanager.com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.mordant.rendering.Theme

class NotSupportedTransferData(
    theme: Theme = Theme.Default
) : CliktError(
    message = theme.danger(
        text = "Not supported transfer data format"
    )
)