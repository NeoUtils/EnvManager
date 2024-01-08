package com.neo.properties.core

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.neo.properties.commands.*

class Properties : NoOpCliktCommand() {

    init {
        subcommands(Install(), Save(), Lister(), Checkout(), Remove())
    }
}