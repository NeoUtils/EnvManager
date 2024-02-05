package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag

class Checkout : CliktCommand(
    help = "Checkout an environment"
) {

    private val tag by tag()

    private val force by option(
        names = arrayOf("-f", "--force"),
        help = "Create environment if it does not exist"
    ).flag()

    override fun run() {

        val installation = requireInstall()

        val config = installation.config

        val target = Target.getOrCreate(config.targetPath)

        val environment = if (force) {
            Environment.getOrCreate(installation.environmentsDir, tag)
        } else {
            Environment(installation.environmentsDir, tag)
        }

        target.write(
            environment
                .read()
                .toProperties()
        )

        config.update {
            it.copy(
                currentEnv = tag
            )
        }
    }
}