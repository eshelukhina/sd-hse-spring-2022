package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class CdTest : CommandExecutorTest() {

    @Test
    fun `cd with arg`() = withTestContext {
        testFullOutput("cd src/test/resources/test_dir", "")
        testFullOutput("ls", "a.txt\nb.txt\nc.txt\ndir\n")
    }

    @Test
    fun `cd without arg`() = withTestContext {
        testFullOutput("cd src/test/resources/test_dir", "")
        testFullOutput("cd", "")
        testFullOutput("cd src/test/resources/test_dir", "")
        testFullOutput("ls", "a.txt\nb.txt\nc.txt\ndir\n")
    }
}
