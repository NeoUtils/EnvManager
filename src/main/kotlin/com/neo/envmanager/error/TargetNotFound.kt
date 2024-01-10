package com.neo.envmanager.error

import com.github.ajalt.clikt.core.CliktError

class TargetNotFound(
    path: String
) : CliktError("Target file not found at '$path'")