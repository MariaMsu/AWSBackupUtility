package actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import utils.S3Interaction

object DeleteBucketAction : Action {
    override val name = "delete-bucket"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "S3 bucket name"
        ).default(Defaults.DEFAULT_BUCKET)
    }

    fun run(bucketName: String) {
        S3Interaction.deleteExistingBucket(bucketName = bucketName)
    }

    /**
    example: delete-bucket
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(DeleteBucketAction::UserArgs)
        run(bucketName = arguments.bucket)
    }
}