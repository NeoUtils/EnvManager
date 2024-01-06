package com.neo.properties

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class Properties : CliktCommand() {

    private val name by argument()

    override fun run() {
        println("Hello $name!")
    }
}