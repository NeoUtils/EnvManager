package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.util.InstallationHelp
import com.neo.envmanager.util.ResultCode
import io.kotest.core.spec.style.ShouldSpec
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

            assertEquals(ResultCode.SUCCESS.code, result.statusCode)
            assertTrue(installation.installed)
        }

        should("install successfully, when run install --target=<path>") {

            val args = args("--path=${projectPath.path}", "install --target=${targetPath.path}")

            val result = envm.test(args)

            assertEquals(ResultCode.SUCCESS.code, result.statusCode)
            assertTrue(installation.installed)
        }
    }

    context("installed") {

        beforeTest {
            installation.install()
        }

        should("don't install, when run install") {

            val args = args("--path=${projectPath.path}", "install")

            val result = envm.test(args, targetPath.path)

            assertEquals(ResultCode.FAILURE.code, result.statusCode)
        }

        should("don't install, when run install --target=<path>") {

            val args = args("--path=${projectPath.path}", "install --target=${targetPath.path}")

            val result = envm.test(args)

            assertEquals(ResultCode.FAILURE.code, result.statusCode)
        }

        should("install successfully, when run install --force") {

            val args = args("--path=${projectPath.path}", "install --force")

            val result = envm.test(args, targetPath.path)

            assertEquals(ResultCode.SUCCESS.code, result.statusCode)
            assertTrue(installation.installed)
        }
    }
})

fun args(vararg args: String) = args.joinToString(" ")
