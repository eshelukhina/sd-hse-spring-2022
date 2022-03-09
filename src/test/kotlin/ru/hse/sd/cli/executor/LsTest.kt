package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test
import ru.hse.sd.cli.command.CodeResult
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.test.assertTrue

@Suppress("SpellCheckingInspection")
class LsTest : CommandExecutorTest() {
    @Test
    fun `ls with arg`() = withTestContext {
        testFullOutput("ls src/test/resources/test_dir", "a.txt\nb.txt\nc.txt\ndir\n")
    }

    @Test
    fun `ls without arg`() = withTestContext {
        testFullOutput("cd src/test/resources/test_dir", "")
        testFullOutput("ls", "a.txt\nb.txt\nc.txt\ndir\n")
    }

    @Test
    fun `ls empty dir`() = withTestContext {
        val directory = createTempDirectory()
        try {
            testFullOutput("ls $directory", "")
        } finally {
            directory.deleteIfExists()
        }
    }

    @Test
    fun `ls file`() = withTestContext {
        val argument = "src/test/resources/test_dir/a.txt"
        testFullOutput("ls $argument", Path.of(argument).toString() + System.lineSeparator())
    }

    @Test
    fun `ls file with extra path separators`() = withTestContext {
        val argument = "src/test/resources/test_dir/a.txt/"
        test("ls $argument", error = "ls: $argument: Not a directory") {
            assertTrue { it is CodeResult && it.code != 0 }
        }
    }

    @Test
    fun `ls with absolute path`() = withTestContext {
        val file = createTempFile().toAbsolutePath()
        try {
            test("ls $file", output = "$file") { assertTrue { it is CodeResult && it.code == 0 } }
        } finally {
            file.deleteIfExists()
        }
    }

    @Test
    fun `ls with non-existent file`() = withTestContext {
        test(
            "ls ${FileContentResources.notExistsFilename}",
            error = "ls: ${FileContentResources.notExistsFilename}: invalid path"
        ) {
            assertTrue { it is CodeResult && it.code != 0 }
        }
    }
}
