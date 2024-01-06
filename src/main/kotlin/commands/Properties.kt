package com.neo.properties.commands

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import commands.Install

class Properties : NoOpCliktCommand() {

    init {
        subcommands(Install(), Save())
    }
}