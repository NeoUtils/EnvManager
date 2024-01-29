package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.neo.envmanager.com.neo.envmanager.util.extension.update
import com.neo.envmanager.core.Command
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import com.neo.envmanager.util.extension.tag

class Checkout : Command(
    help = "Checkout an environment"
) {

    private val tag by tag()

    private val force by option(
        names = arrayOf("-f", "--force"),
        help = "Force checkout"
    ).flag()

    override fun run() {

        val config = requireInstall()

        val target = Target.getOrCreate(config.targetPath)

        val environment = getEnvironment()

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

    private fun getEnvironment(): Environment {

        if (force) {
            return Environment.getOrCreate(paths.environmentsDir, tag)
        }

        return Environment.get(paths.environmentsDir, tag)
    }
}