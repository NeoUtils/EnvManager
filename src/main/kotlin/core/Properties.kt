package com.neo.properties.core

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.neo.properties.commands.Checkout
import com.neo.properties.commands.Install
import com.neo.properties.commands.Lister
import com.neo.properties.commands.Save

class Properties : NoOpCliktCommand() {

    init {
        subcommands(Install(), Save(), Lister(), Checkout())
    }
}