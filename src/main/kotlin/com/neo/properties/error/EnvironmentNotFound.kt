package com.neo.properties.error

import com.github.ajalt.clikt.core.CliktError

class EnvironmentNotFound(
    tag: String
) : CliktError("✖ Environment '$tag' not found")
