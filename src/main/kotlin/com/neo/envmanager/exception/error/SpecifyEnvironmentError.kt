package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.UsageError

class SpecifyEnvironmentError(
    override val message: String
) : UsageError(message) {

    constructor(multiple: Boolean = false) : this(
        if (multiple) "Specify one or more environment tags"
        else "Specify a environment tag"
    )
}