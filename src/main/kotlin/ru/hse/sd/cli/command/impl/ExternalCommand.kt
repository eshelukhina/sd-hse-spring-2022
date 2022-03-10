package ru.hse.sd.cli.command.impl

import ru.hse.sd.cli.command.CodeResult
import ru.hse.sd.cli.command.Command
import ru.hse.sd.cli.command.CommandResult
import ru.hse.sd.cli.env.Environment
import ru.hse.sd.cli.env.IoContext

/**
 * Represents all other external commands in CLI.
 * It tries to invoke an external command, accessible from the terminal, with [name] and [args].
 */
data class ExternalCommand(
    /**
     * Name of the process to be executed.
     */
    override val name: String,
    /**
     * Arguments to be used running the process.
     */
    val args: List<String>
) : Command() {
    override fun execute(context: IoContext, env: Environment): CommandResult {
        val process = ProcessBuilder().apply {
            command(name, *args.toTypedArray())
            directory(env.workingDirectory.toFile())
            environment() += env.variables
        }.start()
        context.input.transferTo(process.outputStream)
        process.waitFor()
        process.inputStream.transferTo(context.output)
        process.errorStream.transferTo(context.error)
        return CodeResult(process.exitValue())
    }
}
