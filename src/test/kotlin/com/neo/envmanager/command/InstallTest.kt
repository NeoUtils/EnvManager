package com.neo.envmanager.command

import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.help.InstallationHelp
import com.neo.envmanager.help.ResultCode
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.Assertions.assertEquals

class InstallTest : FunSpec({

    val installation = InstallationHelp()

    beforeTest {
        installation.clear()
    }

    test("should install successfully") {

        // given

        installation.setup()

        val envm = Envm()

        val projectPath = installation.projectDir.path
        val targetPath = installation.targetFile.path

        // when

        val result = envm.test("--path=$projectPath install", targetPath)

        // then

        assertEquals(ResultCode.SUCCESS.code, result.statusCode)
    }

    test("should not install if already installed") {

        // given

        installation.install()

        val envm = Envm()

        val projectPath = installation.projectDir.path
        val targetPath = installation.targetFile.path

        // when

        val result = envm.test("--path=$projectPath install", targetPath)

        // then

        assertEquals(ResultCode.FAILURE.code, result.statusCode)
    }
})
