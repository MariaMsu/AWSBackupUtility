package Actions

import com.xenomachina.argparser.ArgParser

interface Action {
    val name: String
    class UserArgs(parser: ArgParser)
    fun parseArgsAndRun(commandArgs: Array<String>)
}
