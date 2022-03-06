package ru.hse.sd.cli.command.impl

import ru.hse.sd.cli.command.CodeResult
import ru.hse.sd.cli.command.Command
import ru.hse.sd.cli.command.CommandResult
import ru.hse.sd.cli.env.Environment
import ru.hse.sd.cli.env.IoContext
import ru.hse.sd.cli.util.write

import java.nio.file.Path
import kotlin.io.path.isDirectory


/**
 * Represents the `cd`-command in CLI.
 * Changes current working directory by moving to provided subfolder.
 */
data class CdCommand(
    /**
     * Possible path of the directory.
     * If the path is not provided, then home directory is used instead.
     */
    val argument: String?
) : Command() {
    override fun execute(context: IoContext, env: Environment): CommandResult {
        if (argument == null) {
            env.workingDirectory = Path.of("").toAbsolutePath()
            return CodeResult.success
        }
        val expectedPath = env.workingDirectory.resolve(argument)
        if (!expectedPath.isDirectory()) {
            context.error.write("cd: ${expectedPath}: invalid input")
            return CodeResult.internalError
        }
        env.workingDirectory = expectedPath.normalize()
        return CodeResult.success
    }
}
