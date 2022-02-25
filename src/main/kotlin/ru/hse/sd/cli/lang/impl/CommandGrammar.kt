package ru.hse.sd.cli.lang.impl

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser
import ru.hse.sd.cli.command.*
import ru.hse.sd.cli.command.impl.*

/**
 * Bash-like CLI command grammar.
 * Can be used for lexing and parsing.
 */
object CommandGrammar : Grammar<Command>() {
    @Suppress("unused")
    internal val wsToken by regexToken("\\s+", ignore = true)
    internal val pipeToken by literalToken("|")
    internal val equalToken by literalToken("=")
    internal val substitutionToken by regexToken("\\$[^\\s=|\"'\$]+")
    internal val lsToken by literalToken("ls")
    internal val catToken by literalToken("cat")
    internal val echoToken by literalToken("echo")
    internal val wcToken by literalToken("wc")
    internal val pwdToken by literalToken("pwd")
    internal val exitToken by literalToken("exit")
    internal val grepToken by literalToken("grep")
    internal val quoteToken by regexToken("'[^']*'")
    internal val doubleQuoteToken by regexToken("\"[^\"]*\"")
    internal val identifierToken by regexToken("[^\\s=|\"'$]+")

    private val weakCommandName by catToken or echoToken or wcToken or pwdToken or exitToken or lsToken
    private val literal by identifierToken or quoteToken or doubleQuoteToken or weakCommandName map {
        when (it.type) {
            quoteToken -> it.text.removeSurrounding("'")
            doubleQuoteToken -> it.text.removeSurrounding("\"")
            else -> it.text
        }
    }

    private val lsTerm by lsToken and optional(literal) map { LsCommand(it.t2) }
    private val echoTerm by echoToken and zeroOrMore(literal) map { EchoCommand(it.t2) }
    private val catTerm by catToken and optional(literal) map { CatCommand(it.t2) }
    private val wcTerm by wcToken and optional(literal) map { WcCommand(it.t2) }
    private val pwdTerm by pwdToken and optional(literal) map { PwdCommand }
    private val exitTerm by exitToken and optional(literal) map { ExitCommand }
    private val grepTerm by grepToken and zeroOrMore(literal) map { GrepCommand(it.t2) }
    private val externalCommandTerm by literal and zeroOrMore(literal) map { (name, args) ->
        ExternalCommand(name, args)
    }
    private val term by lsTerm or echoTerm or catTerm or wcTerm or pwdTerm or exitTerm or grepTerm or externalCommandTerm

    private val pipeChain by leftAssociative(term, pipeToken) { l, _, r ->
        PipeCommand(l, r)
    }

    private val assignment by literal and equalToken and literal map { (left, _, right) ->
        AssignmentCommand(left, right)
    }

    override val rootParser: Parser<Command> by assignment or pipeChain
}
