package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.neo.envmanager.Envm
import com.neo.envmanager.exception.error.NotInstalledError
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.InstallationHelp
import com.neo.envmanager.util.ResultCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SetterTest : ShouldSpec({

    val installation = InstallationHelp()

    val (projectDir, _) = installation

    val envm = Envm()

    beforeTest {
        installation.clear()
    }

    afterSpec {
        installation.clear()
    }

    context("installed") {

        beforeTest {
            installation.install()
        }

        should("set successfully in current environment") {

            installation.updateConfig {
                it.copy(currentEnv = "test")
            }

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environment

            val environment = Environment(installation.paths.environmentsDir, "test")

            environment.read() shouldBe mapOf("KEY" to "VALUE")

            // check target

            val target = Target(installation.targetFile)

            target.read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("don't set, when no specify environment") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            // check result

            result.statusCode shouldBe ResultCode.FAILURE.code
        }

        should("set successfully in specified environment") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --tag=test")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environment

            val environment = Environment(installation.paths.environmentsDir, "test")

            environment.read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("set successfully only on target") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --target-only")

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check target

            val target = Target(installation.targetFile)

            target.read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("set successfully in all environments") {

            val tags = listOf("test1", "test2", "test3")

            val environments = tags.map {
                Environment.getOrCreate(
                    installation.paths.environmentsDir, it
                )
            }

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --all")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environments

            environments.forEach {
                it.read() shouldBe mapOf("KEY" to "VALUE")
            }
        }
    }

    context("not installed") {

         should("return correct error message") {

             // run

             val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

             // check result

             result.statusCode shouldBe ResultCode.FAILURE.code

             // check output

             result.stderr.trimEnd() shouldBe "✖ Not installed"
         }
    }

})
