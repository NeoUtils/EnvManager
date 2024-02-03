package com.neo.envmanager.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.neo.envmanager.model.Paths

abstract class Command(
    name: String? = null,
    help: String
) : CliktCommand(name = name, help = help) {

    protected val paths by requireObject<Paths>()
}