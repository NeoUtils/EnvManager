package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
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

        should("don't set, when no specify environment") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            result.statusCode shouldBe ResultCode.FAILURE.code
        }

        should("set successfully in current environment") {

            installation.updateConfig { it.copy(currentEnv = "test") }

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE")

            result.statusCode shouldBe ResultCode.SUCCESS.code
        }

        should("set successfully in specified environment") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --tag=test")

            result.statusCode shouldBe ResultCode.SUCCESS.code
        }

        should("set successfully in target") {

            val result = envm.test("--path=${projectDir.path} set KEY=VALUE --target-only")

            result.statusCode shouldBe ResultCode.SUCCESS.code
        }
    }

})
