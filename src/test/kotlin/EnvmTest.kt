import com.github.ajalt.clikt.testing.test
import com.neo.envmanager.Envm
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
            assertEquals(randomVersion, result.output.trimEnd())
            assertEquals(ResultCode.SUCCESS.code, result.statusCode)
        }

        unmockkObject(Package)
    }

    enum class ResultCode(val code: Int) {
        SUCCESS(code = 0),
        FAILURE(code = 1)
    }
}


fun genRandomVersion(): String {

    return (0..2).map {
        (0..9).random()
    }.joinToString(".")
}