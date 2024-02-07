import com.github.ajalt.clikt.testing.test
import com.google.gson.Gson
import com.neo.envmanager.Envm
import com.neo.envmanager.model.Config
import com.neo.envmanager.model.Installation
import com.neo.envmanager.model.Paths
import com.neo.envmanager.util.Package
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class EnvmTest {

    @Test
    fun `given -v or --version argument, should return program version`() {

        // given

        val envm = Envm()

        mockkObject(Package)

        val randomVersion = genRandomVersion()

        every { Package.version } returns randomVersion

        // when

        val results = listOf(
            envm.test("-v"),
            envm.test("--version")
        )

        // then

        for (result in results) {
            assertEquals(ResultCode.SUCCESS.code, result.statusCode)
            assertEquals(randomVersion, result.output.trimEnd())
        }

        unmockkObject(Package)
    }

    @Test
    fun `given -c or --show-config argument, should return config file path`() {

        val envm = Envm()

        val config = Config(
            targetPath = "target_path",
            currentEnv = "current_path"
        )

        testInstallation(config) {

            val results = listOf(
                envm.test("--path=build/tmp/test -c"),
                envm.test("--path=build/tmp/test --show-config")
            )

            for (result in results) {

                assertEquals(ResultCode.SUCCESS.code, result.statusCode)

                assertEquals(
                    listOf(
                        "target: target_path",
                        "current: current_path"
                    ),
                    result.output.trimEnd().split("\n")
                )
            }
        }
    }

    private fun testInstallation(
        config: Config,
        toTestDir: File = File("build/tmp/test"),
        block: (Installation) -> Unit
    ) {

        val paths = Paths(toTestDir).apply {
            installationDir.mkdir()
            environmentsDir.mkdir()

            configFile.writeText(
                Gson().toJson(
                    config
                )
            )
        }

        block(
            Installation(
                config = config,
                environmentsDir = paths.environmentsDir
            )
        )

        toTestDir.deleteRecursively()
    }

}


fun genRandomVersion(): String {

    return (0..2).map {
        (0..9).random()
    }.joinToString(".")
}