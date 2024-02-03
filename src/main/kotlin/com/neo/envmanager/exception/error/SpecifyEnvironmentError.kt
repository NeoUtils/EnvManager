package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.UsageError

class SpecifyEnvironmentError(
    multiple: Boolean = false
) : UsageError(
    message = if (multiple)
        "Specify one or more environment tags"
    else
        "Specify a environment tag"
)