import Actions.*
import kotlin.system.exitProcess


/**
Before running this Kotlin code example, set up your development environment,
including your credentials.
For more information, see the following documentation topic:
https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
 */
fun main(args: Array<String>) {
//    println("args: ${args.joinToString(", ")}")

    val usage = """
    Usage:
        <bucketName> 
    Where:
        bucketName - The name of the Amazon S3 bucket to create. The Amazon S3 bucket name must be unique, or an error occurs.
    """

    if (args.isEmpty()) {
        println(usage)
        exitProcess(-1)
    }
    val command = args[0]
    val commandArgs = args.drop(1).toTypedArray()

    val command2action = mapOf<String, Action>(
        ListBucketAction.name to ListBucketAction,
        StoreAction.name to StoreAction,
        RestoreAction.name to RestoreAction,
        RestoreFileAction.name to RestoreFileAction,
        DeleteBackupAction.name to DeleteBackupAction,
        DeleteBucketAction.name to DeleteBucketAction
    )
    val action = command2action[command]
    if (action != null) {
        action.parseArgsAndRun(commandArgs)
    } else {
        println("") // TODO
    }
}
