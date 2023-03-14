package Actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

object ListBucketAction : Action {
    override val name = "list-bucket"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)
    }

    fun run(bucketName: String) {
        S3Interaction.listBucketObjects(bucketName = bucketName)
    }

    /**
    example: list-bucket
     **/
    override fun parseArgsAndRun(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(::UserArgs)
        run(bucketName = arguments.bucket)
    }
}