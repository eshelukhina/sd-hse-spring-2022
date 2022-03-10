package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test
import ru.hse.sd.cli.command.CodeResult
import kotlin.test.assertTrue

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

    @Test
    fun `cd with non-existent directory`() = withTestContext {
        test(
            "cd ${FileContentResources.notExistsDirectory}",
            error = "cd: ${FileContentResources.notExistsDirectory}: No such file or directory"
        ) {
            assertTrue { it is CodeResult && it.code != 0 }
        }
    }

    @Test
    fun `cd with file`() = withTestContext {
        test(
            "cd ${FileContentResources.hellosFilename}",
            error = "cd: ${FileContentResources.hellosFilename}: Not a directory"
        ) {
            assertTrue { it is CodeResult && it.code != 0 }
        }
    }
}
