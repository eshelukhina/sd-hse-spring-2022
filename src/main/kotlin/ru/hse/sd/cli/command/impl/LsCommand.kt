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
 * Lists files and directories within working directory
 */
data class LsCommand(
    /**
     * Possible path of the file or directory.
     * If the path is not provided, then path to working directory is used instead.
     */
    val argument: String?
) : Command() {
    override fun execute(context: IoContext, env: Environment): CommandResult {
        val directoryContents = { path: Path ->
            path.listDirectoryEntries().stream().map { p -> p.name }.sorted()
                .reduce { a, b -> a + System.lineSeparator() + b }.orElse("") + System.lineSeparator()
        }
        if (argument == null) {
            context.output.write(directoryContents(env.workingDirectory))
            return CodeResult.success
        }
        val expectedPath = env.workingDirectory.resolve(argument)
        if (expectedPath.isRegularFile()) {
            context.output.write(argument + System.lineSeparator())
            return CodeResult.success
        }
        if (expectedPath.isDirectory()) {
            context.output.write(directoryContents(expectedPath))
            return CodeResult.success
        }
        context.error.write("ls: ${argument}: invalid path" + System.lineSeparator())
        return CodeResult.internalError
    }
}
