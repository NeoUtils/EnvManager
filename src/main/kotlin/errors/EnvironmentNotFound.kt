package com.neo.properties.errors

import com.github.ajalt.clikt.core.CliktError

class EnvironmentNotFound(
    tag: String
) : CliktError("✖ Environment '$tag' not found")
