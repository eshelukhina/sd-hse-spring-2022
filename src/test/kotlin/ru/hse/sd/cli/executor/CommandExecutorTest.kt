package ru.hse.sd.cli.executor

import ru.hse.sd.cli.command.CodeResult
import ru.hse.sd.cli.command.CommandExecutor
import ru.hse.sd.cli.command.CommandResult
import ru.hse.sd.cli.util.write
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.test.assertEquals

abstract class CommandExecutorTest {
    fun TestContext.test(pair: Pair<String, String>) = test(pair.first, pair.second)

    fun TestContext.test(
        command: String,
        output: String? = null,
        error: String? = null,
        expectedResult: (CommandResult) -> Unit
    ) {
        val result = execute(command)
        expectedResult(result)
        if (output != null) assertEquals(output, outputLine())
        if (error != null) assertEquals(error, errorLine())
    }

    fun TestContext.test(
        command: String,
        output: String? = null,
        error: String? = null,
        expectedResult: CommandResult = CodeResult.success
    ) = test(command, output, error) { assertEquals(it, expectedResult) }

    fun withTestContext(block: TestContext.() -> Unit) {
        val input = PipedInputStream()
        val output = PipedOutputStream()
        val error = PipedOutputStream()
        val context = TestContext(
            toInput = PipedOutputStream(input),
            fromOutput = PipedInputStream(output),
            fromError = PipedInputStream(error),
            executor = CommandExecutor(input, output, error)
        )
        context.block()
    }

    class TestContext internal constructor(
        private val toInput: OutputStream,
        fromOutput: InputStream,
        fromError: InputStream,
        private val executor: CommandExecutor
    ) {
        private val fromOutput = fromOutput.bufferedReader()
        private val fromError = fromError.bufferedReader()

        fun input(input: String) = toInput.write("$input\n")
        fun outputLine(): String = fromOutput.readLine()
        fun errorLine(): String = fromError.readLine()
        fun execute(command: String) = executor.execute(command)
    }
}