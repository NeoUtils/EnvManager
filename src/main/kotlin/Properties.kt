package com.neo.properties

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.neo.properties.commands.Install
import com.neo.properties.commands.Save

class Properties : NoOpCliktCommand() {

    init {
        subcommands(Install(), Save())
    }
}