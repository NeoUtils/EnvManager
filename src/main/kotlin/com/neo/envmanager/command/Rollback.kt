package com.neo.envmanager.command

import com.neo.envmanager.com.neo.envmanager.exception.error.NoCurrentEnvironment
import com.neo.envmanager.core.Command
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.extension.requireInstall
import extension.getOrThrow

class Rollback : Command(
    help = "Rollback the target"
) {

    override fun run() {

        val config = requireInstall()

        val target = Target.getOrCreate(config.targetPath)

        val tag = config.currentEnv ?: throw NoCurrentEnvironment()

        val environment = Environment(paths.environmentsDir, tag)

        target.write(environment.read().toProperties())
    }
}