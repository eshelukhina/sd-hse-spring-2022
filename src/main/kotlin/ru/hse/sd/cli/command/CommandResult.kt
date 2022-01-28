package ru.hse.sd.cli.command

sealed interface CommandResult

object ExitResult : CommandResult

data class CodeResult(val code: Int) : CommandResult {
    companion object {
        val success = CodeResult(0)
    }
}