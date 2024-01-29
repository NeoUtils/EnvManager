package com.neo.envmanager.command

import com.github.ajalt.clikt.parameters.options.option
import com.neo.envmanager.core.Command
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall

class Rollback : Command(
    help = "Rollback the target"
) {

    private val tag by option(
        names = arrayOf("-t", "--tag"),
        help = "Environment tag"
    )

    override fun run() {

        val config = requireInstall()

        val target = Target.getOrCreate(config.targetPath)

        val tag = tag ?: config.currentEnv ?: throw SpecifyEnvironmentError()

        val environment = Environment.get(paths.environmentsDir, tag)

        target.write(environment.read().toProperties())
    }
}