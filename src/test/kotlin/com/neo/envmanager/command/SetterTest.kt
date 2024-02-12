package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.exception.error.CanNotFindEnvironments
import com.neo.envmanager.exception.error.NotInstalledError
import com.neo.envmanager.exception.error.SpecifyEnvironmentError
import com.neo.envmanager.model.Environment
import com.neo.envmanager.model.Target
import com.neo.envmanager.util.InstallationHelp
import com.neo.envmanager.util.ResultCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith

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

            // given

            installation.updateConfig {
                it.copy(currentEnv = "test")
            }

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environment

            Environment(
                installation.paths.environmentsDir,
                tag = "test"
            ).read() shouldBe mapOf("KEY" to "VALUE")

            // check target

            Target(
                installation.targetFile
            ).read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("don't set, when no specify environment") {

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            // check result

            result.statusCode shouldBe ResultCode.FAILURE.code


            // check error

            result.stderr.trimEnd() shouldEndWith SpecifyEnvironmentError().message
        }

        should("set successfully in specified environment") {

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --tag=test")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environment
            Environment(
                installation.paths.environmentsDir,
                tag = "test"
            ).read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("set successfully only on target") {

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --target-only")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check target

            Target(
                installation.targetFile
            ).read() shouldBe mapOf("KEY" to "VALUE")
        }

        should("set successfully in all environments") {

            // given

            val tags = listOf("test1", "test2", "test3")

            val environments = tags.map {
                Environment.getOrCreate(
                    installation.paths.environmentsDir, it
                )
            }

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --all")

            // check result

            result.statusCode shouldBe ResultCode.SUCCESS.code

            // check environments

            for (environment in environments) {
                environment.read() shouldBe mapOf("KEY" to "VALUE")
            }
        }

        should("return error correctly, when run 'set in all' without environments") {

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --all")

            // check result

            result.statusCode shouldBe ResultCode.FAILURE.code

            // check error

            result.stderr.trimEnd() shouldBe CanNotFindEnvironments().message
        }
    }

    context("not installed") {

        should("return correct error message") {

            // run

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --tag=test")

            // check result

            result.statusCode shouldBe ResultCode.FAILURE.code

            // check error

            result.stderr.trimEnd() shouldBeEqual NotInstalledError().message
        }
    }

})
