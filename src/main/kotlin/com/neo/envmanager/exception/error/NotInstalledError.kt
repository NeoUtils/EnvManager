package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.CliktError

class NotInstalledError(
    override val message: String = "✖ Not installed"
) : CliktError(message)