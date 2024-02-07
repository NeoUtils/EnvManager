package com.neo.envmanager.command

import ResultCode
import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.model.Paths
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class InstallTest {

    private val installation = InstallationHelp()

    @BeforeEach
    @AfterEach
    fun setup() {
        installation.clear()
    }

    @Test
    fun `should install successfully`() {

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

    @Test
    fun `should not install if already installed`() {

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
}