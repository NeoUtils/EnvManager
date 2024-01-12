package com.neo.envmanager.exception.error

import com.github.ajalt.clikt.core.UsageError

class SpecifyEnvironmentError : UsageError(message = "Specify environment with <tag>")