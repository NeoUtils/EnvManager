package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.UsageError

class SpecifyKeysError : UsageError(
    message = "Specify one or more keys"
)