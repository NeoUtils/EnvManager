import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
import com.neo.envmanager.command.installed
import com.neo.envmanager.util.Package
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

        installed {

            updateConfig {
                it.copy(
                    currentEnv = "current_path"
                )
            }

            val projectPath = projectDir.path

            val results = listOf(
                envm.test("--path=$projectPath -c"),
                envm.test("--path=$projectPath --show-config")
            )

            for (result in results) {

                assertEquals(ResultCode.SUCCESS.code, result.statusCode)

                assertEquals(
                    listOf(
                        "target: build/tmp/test/test.properties",
                        "current: current_path"
                    ),
                    result.output.trimEnd().split("\n")
                )
            }
        }
    }

}

fun genRandomVersion(): String {

    return (0..2).map {
        (0..9).random()
    }.joinToString(".")
}