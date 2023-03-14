package actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

object DeleteBackupAction : Action {
    override val name = "delete-backup"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in S3"
        )
    }

    fun run(bucketName: String, objectKey: String) {
        S3Interaction.deleteBucketObjects(bucketName = bucketName, objectName = objectKey)
    }

    /**
    example: delete-backup -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(DeleteBackupAction::UserArgs)
        run(bucketName = arguments.bucket, objectKey = arguments.key)
    }
}