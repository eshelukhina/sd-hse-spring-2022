package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class LsTest : CommandExecutorTest() {
    @Test
    fun `ls with arg`() = withTestContext {
        testFullOutput("ls src/test/resources/test_dir", "a.txt\nb.txt\nc.txt\ncrazy_dir\ndir\n")
    }

    @Test
    fun `ls without arg`() = withTestContext {
        testFullOutput("cd src/test/resources/test_dir", "")
        testFullOutput("ls", "a.txt\nb.txt\nc.txt\ncrazy_dir\ndir\n")
    }

    @Test
    fun `ls empty dir`() = withTestContext {
        testFullOutput("ls src/test/resources/test_dir/crazy_dir", "")
    }

    @Test
    fun `ls file`() = withTestContext {
        testFullOutput("ls src/test/resources/test_dir/a.txt", "src/test/resources/test_dir/a.txt\n")
    }
}
