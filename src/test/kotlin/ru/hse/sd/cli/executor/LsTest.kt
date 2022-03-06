package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteIfExists

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
        }
        finally {
            directory.deleteIfExists()
        }
    }

    @Test
    fun `ls file`() = withTestContext {
        testFullOutput("ls src/test/resources/test_dir/a.txt", "src/test/resources/test_dir/a.txt\n")
    }
}
