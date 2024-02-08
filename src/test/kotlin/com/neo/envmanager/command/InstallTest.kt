package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.google.gson.Gson
import com.neo.envmanager.Envm
import com.neo.envmanager.model.Config
import com.neo.envmanager.util.InstallationHelp
import com.neo.envmanager.util.ResultCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class InstallTest : ShouldSpec({

    val envm = Envm()

    val installation = InstallationHelp()

    val (projectPath, targetPath) = installation

    beforeTest { installation.clear() }

    afterSpec { installation.clear() }

    context("uninstalled") {

        beforeTest {
            installation.setup()
        }

        should("install successfully, when run install") {

            val args = args("--path=${projectPath.path}", "install")

            val result = envm.test(args, targetPath.path)

            ResultCode.SUCCESS.code shouldBe result.statusCode

            installation.check()
        }

        should("install successfully, when run install --target=<path>") {

            val args = args("--path=${projectPath.path}", "install --target=${targetPath.path}")

            val result = envm.test(args)

            ResultCode.SUCCESS.code shouldBe  result.statusCode

            installation.check()
        }
    }

    context("installed") {

        beforeTest {
            installation.install()
        }

        should("don't install, when run install") {

            val args = args("--path=${projectPath.path}", "install")

            val result = envm.test(args, targetPath.path)

            ResultCode.FAILURE.code shouldBe result.statusCode
        }

        should("don't install, when run install --target=<path>") {

            val args = args("--path=${projectPath.path}", "install --target=${targetPath.path}")

            val result = envm.test(args)

            ResultCode.FAILURE.code shouldBe result.statusCode
        }

        should("install successfully, when run install --force") {

            val args = args("--path=${projectPath.path}", "install --force")

            val result = envm.test(args, targetPath.path)

            ResultCode.SUCCESS.code shouldBe result.statusCode

            installation.check()
        }
    }
})

fun args(vararg args: String) = args.joinToString(" ")

fun InstallationHelp.check() {

    assertTrue(ready, "Not ready")
    assertTrue(installed, "Not installed")
    assertTrue(paths.configFile.exists(), "Config file not found")
    assertTrue(paths.gitIgnoreFile.exists(), ".gitignore file not found")

    val config = Config.loadFrom(paths.configFile)

    assertEquals(targetFile.path, config.targetFile.path)
}