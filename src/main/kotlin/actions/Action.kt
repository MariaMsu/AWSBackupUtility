package actions

interface Action {
    val name: String
    fun parseArgsAndRun(commandArgs: Array<String>)
}
