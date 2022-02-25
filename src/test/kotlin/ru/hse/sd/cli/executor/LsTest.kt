package ru.hse.sd.cli.executor

import org.junit.jupiter.api.Test
import ru.hse.sd.cli.command.CodeResult
import kotlin.test.assertTrue

@Suppress("SpellCheckingInspection")
class LsTest : CommandExecutorTest() {
    @Test
    fun `Simple eng ls test`() = withTestContext {
        testFullOutput("ls", "")
    }
}
