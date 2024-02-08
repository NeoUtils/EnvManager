package com.neo.envmanager.command

import com.github.ajalt.clikt.core.CliktCommand
import com.neo.envmanager.com.neo.envmanager.exception.error.NoCurrentEnvironment
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.FilePromise
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall

class Rollback : CliktCommand(
    help = "Rollback the target"
) {

    override fun run() {

        val (config, environmentsDir) = requireInstall()

        val target = Target.getOrCreate(FilePromise(config.targetFile))

        val tag = config.currentEnv ?: throw NoCurrentEnvironment()

        val environment = Environment(environmentsDir, tag)

        target.write(environment.read().toProperties())
    }
}