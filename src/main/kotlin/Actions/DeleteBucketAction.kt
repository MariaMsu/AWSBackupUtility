package Actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

object DeleteBucketAction : Action {
    override val name = "delete-bucket"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)
    }

    fun run(bucketName: String) {
        S3Interaction.deleteExistingBucket(bucketName = bucketName)
    }

    /**
    example: delete-bucket
     **/
    override fun parseArgsAndRun(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(DeleteBucketAction::UserArgs)
        run(bucketName = arguments.bucket)
    }
}