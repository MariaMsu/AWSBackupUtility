import actions.*
import kotlin.system.exitProcess


val command2action = mapOf<String, Action>(
    ListBucketAction.name to ListBucketAction,
    StoreAction.name to StoreAction,
    RestoreAction.name to RestoreAction,
    RestoreFileAction.name to RestoreFileAction,
    DeleteBackupAction.name to DeleteBackupAction,
    DeleteBucketAction.name to DeleteBucketAction
)

val usage = """
Usage:
    <command> <command_arguments>
Where:
    Available commands are: ${command2action.keys.joinToString(", ")}
"""

/**
 * read the arguments from the console and call corresponding commands
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(usage)
        exitProcess(-1)
    }

    // split args into <command> and <commandArgs>
    val command = args[0]
    val commandArgs = args.drop(1).toTypedArray()

    val action = command2action[command]
    if (action == null) {
        println(usage)
        exitProcess(-1)
    }
    try {
        if (action.parseArgsAndCall(commandArgs)) {
            exitProcess(0)
        } else {
            exitProcess(-1)
        }
    } catch (e: Exception) {
        println(e.message)
        exitProcess(-1)
    }
}
