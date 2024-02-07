package com.neo.envmanager.command

import ResultCode
import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.model.Paths
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class InstallTest {

    private val testDir = File("build/tmp/test")
    private val targetFile = File(testDir, "test.properties")

    private val paths = Paths(testDir)

    @AfterEach
    fun setup() {
        testDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    @Test
    fun `should install successfully`() {

        // given

        targetFile.createNewFile()
        paths.installationDir.deleteRecursively()

        val envm = Envm()

        // when

        val result = envm.test("--path=${testDir.path} install", targetFile.path)

        assertEquals(ResultCode.SUCCESS.code, result.statusCode)
    }

    @Test
    fun `should not install if already installed`() {

        // given

        targetFile.createNewFile()
        paths.installationDir.mkdir()
        paths.configFile.createNewFile()

        val envm = Envm()

        // when

        val result = envm.test("--path=${testDir.path} install", targetFile.path)

        assertEquals(ResultCode.FAILURE.code, result.statusCode)
    }
}