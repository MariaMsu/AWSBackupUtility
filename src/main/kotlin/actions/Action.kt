package actions

interface Action {
    // the name by which the implementing function can be called
    val name: String

    /**
     * @param commandArgs consists of strings specified in UserArgs field in each implementing class
     */
    fun parseArgsAndCall(commandArgs: Array<String>): Boolean
}
