package ru.hse.sd.cli.command.impl

import ru.hse.sd.cli.command.CodeResult
import ru.hse.sd.cli.command.Command
import ru.hse.sd.cli.command.CommandResult
import ru.hse.sd.cli.env.Environment
import ru.hse.sd.cli.env.IoContext
import ru.hse.sd.cli.util.write

import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

/**
 * Represents the `ls`-command in CLI.
 */
data class LsCommand(
    /**
     * Possible name of the file, contents of which are printed to output.
     * If the file is not provided, then input is used instead.
     */
    val argument: String?
) : Command() {
    override fun execute(context: IoContext, env: Environment): CommandResult {
        val execute = { path: Path ->
            path.listDirectoryEntries().stream().map { p -> p.name }.sorted()
                .reduce { a, b -> a + "\n" + b }.orElse("")
        }
        if (argument == null) {
            context.output.write(execute(env.workingDirectory))
            return CodeResult.success
        }
        val expectedPath = env.workingDirectory.resolve(argument)
        if (expectedPath.isRegularFile()) {
            context.output.write(expectedPath.toString())
            return CodeResult.success
        }
        if (expectedPath.isDirectory()) {
            context.output.write(execute(expectedPath))
            return CodeResult.success
        }
        context.error.write("ls: ${argument}: invalid path")
        return CodeResult.internalError
    }
}
