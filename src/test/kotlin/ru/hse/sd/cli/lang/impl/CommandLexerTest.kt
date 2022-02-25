package ru.hse.sd.cli.lang.impl

import com.github.h0tk3y.betterParse.lexer.Token
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class CommandLexerTest {

    @ParameterizedTest
    @MethodSource("testSource")
    fun `test tokenize`(input: String, expected: List<Pair<Token, String>>) {
        assertEquals(
            expected,
            CommandLexer().tokenize(input)
                .filterNot { it.type.ignored }
                .map { it.type to it.text }
                .toList()
        )
    }

    companion object {
        @JvmStatic
        fun testSource() = listOf(
            Arguments.of("ls", listOf(CommandGrammar.lsToken to "ls")),
            Arguments.of("echo", listOf(CommandGrammar.echoToken to "echo")),
            Arguments.of("cat", listOf(CommandGrammar.catToken to "cat")),
            Arguments.of("wc", listOf(CommandGrammar.wcToken to "wc")),
            Arguments.of("pwd", listOf(CommandGrammar.pwdToken to "pwd")),
            Arguments.of("exit", listOf(CommandGrammar.exitToken to "exit")),
            Arguments.of("firefox", listOf(CommandGrammar.identifierToken to "firefox")),
            Arguments.of("chrome", listOf(CommandGrammar.identifierToken to "chrome")),
            Arguments.of("telegram", listOf(CommandGrammar.identifierToken to "telegram")),
            Arguments.of("\n\r\t\n   echo       \n\r\n\t\n    ", listOf(CommandGrammar.echoToken to "echo")),
            Arguments.of(
                "echo arg1 arg2", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.identifierToken to "arg1",
                    CommandGrammar.identifierToken to "arg2",
                )
            ),
            Arguments.of(
                "echo 'text'", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.quoteToken to "'text'"
                )
            ),
            Arguments.of(
                "echo 'text1' 'text2'", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.quoteToken to "'text1'",
                    CommandGrammar.quoteToken to "'text2'"
                )
            ),
            Arguments.of(
                "echo 'text \"wow\"'", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.quoteToken to "'text \"wow\"'"
                )
            ),
            Arguments.of(
                "echo \"text\"", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.doubleQuoteToken to "\"text\""
                )
            ),
            Arguments.of(
                "echo \"text1\" \"text2\"", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.doubleQuoteToken to "\"text1\"",
                    CommandGrammar.doubleQuoteToken to "\"text2\""
                )
            ),
            Arguments.of(
                "echo \"text 'wow'\"", listOf(
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.doubleQuoteToken to "\"text 'wow'\""
                )
            ),
            Arguments.of(
                "cat abc/hello.txt", listOf(
                    CommandGrammar.catToken to "cat",
                    CommandGrammar.identifierToken to "abc/hello.txt"
                )
            ),
            Arguments.of(
                "cat a | echo b", listOf(
                    CommandGrammar.catToken to "cat",
                    CommandGrammar.identifierToken to "a",
                    CommandGrammar.pipeToken to "|",
                    CommandGrammar.echoToken to "echo",
                    CommandGrammar.identifierToken to "b"
                )
            ),
            Arguments.of(
                "x=\$y", listOf(
                    CommandGrammar.identifierToken to "x",
                    CommandGrammar.equalToken to "=",
                    CommandGrammar.substitutionToken to "\$y",
                )
            ),
            Arguments.of(
                "\$x\$y", listOf(
                    CommandGrammar.substitutionToken to "\$x",
                    CommandGrammar.substitutionToken to "\$y",
                )
            ),
            Arguments.of("grep", listOf(CommandGrammar.grepToken to "grep")),
            Arguments.of(
                "grep file", listOf(
                    CommandGrammar.grepToken to "grep",
                    CommandGrammar.identifierToken to "file",
                )
            ),
            Arguments.of(
                "grep -i \"минимальный\" README.md", listOf(
                    CommandGrammar.grepToken to "grep",
                    CommandGrammar.identifierToken to "-i",
                    CommandGrammar.doubleQuoteToken to "\"минимальный\"",
                    CommandGrammar.identifierToken to "README.md",
                )
            ),
            Arguments.of(
                "grep -A 1 \"II\" README.md", listOf(
                    CommandGrammar.grepToken to "grep",
                    CommandGrammar.identifierToken to "-A",
                    CommandGrammar.identifierToken to "1",
                    CommandGrammar.doubleQuoteToken to "\"II\"",
                    CommandGrammar.identifierToken to "README.md",
                )
            ),
        )
    }
}
