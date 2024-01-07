package com.neo.properties.errors

import com.github.ajalt.clikt.core.CliktError

class TargetNotFound(
    path: String
) : CliktError("Target file not found at '$path'")