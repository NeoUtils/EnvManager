package com.neo.properties.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import java.io.File

abstract class Command(
    name: String? = null,
    help: String
) : CliktCommand(name = name, help = help) {

    protected val pathDir by requireObject<File>()

}