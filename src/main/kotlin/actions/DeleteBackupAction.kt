package actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import utils.S3Interaction

object DeleteBackupAction : Action {
    override val name = "delete-backup"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "S3 bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in the S3 bucket"
        )
    }

    fun run(bucket: String, key: String) {
        S3Interaction.deleteBucketObjects(bucketName = bucket, objectName = key)
    }

    /**
    how to call from bash:
    $ delete-backup -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>): Boolean {
        val arguments = ArgParser(commandArgs).parseInto(DeleteBackupAction::UserArgs)
        run(bucket = arguments.bucket, key = arguments.key)
        println("${arguments.key} was deleted from ${arguments.bucket}")
        return true
    }
}