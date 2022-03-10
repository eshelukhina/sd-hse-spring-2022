package ru.hse.sd.cli.command.impl

import ru.hse.sd.cli.command.CodeResult
import ru.hse.sd.cli.command.Command
import ru.hse.sd.cli.command.CommandResult
import ru.hse.sd.cli.env.Environment
import ru.hse.sd.cli.env.IoContext
import ru.hse.sd.cli.util.write
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

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
        val expectedPath = env.workingDirectory.resolve(argument).normalize()
        if (!expectedPath.exists()) {
            context.error.write("ls: ${argument}: Invalid path" + System.lineSeparator())
            return CodeResult.internalError
        }
        if (argument.endsWith('/') or argument.endsWith('\\')) {
            return if (expectedPath.isDirectory()) {
                context.output.write(directoryContents(expectedPath))
                CodeResult.success
            } else {
                context.error.write("ls: ${argument}: Not a directory" + System.lineSeparator())
                CodeResult.internalError
            }
        }
        if (expectedPath.isRegularFile()) {
            context.output.write(Paths.get(argument).normalize().toString() + System.lineSeparator())
            return CodeResult.success
        }
        context.output.write(directoryContents(expectedPath))
        return CodeResult.success
    }
}
