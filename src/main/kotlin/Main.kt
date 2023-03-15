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
Before running this Kotlin code example, set up your development environment,
including your credentials.
For more information, see the following documentation topic:
https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
 */
fun main(args: Array<String>) {
//    var envVar: String = System.getenv("AWS_ACCESS_KEY_ID")
//    println("envVar: ${envVar}")

    if (args.isEmpty()) {
        println(usage)
        exitProcess(-1)
    }

    val command = args[0]
    val commandArgs = args.drop(1).toTypedArray()
    val action = command2action[command]
    if (action != null) {
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
    } else {
        println(usage)
        exitProcess(-1)
    }
}
