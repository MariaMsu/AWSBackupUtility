package actions

import aws.sdk.kotlin.services.s3.model.S3Exception
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

    fun run(bucket: String) {
        S3Interaction.deleteExistingBucket(bucketName = bucket)
    }

    /**
    how to call from bash:
    $ delete-bucket
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(DeleteBucketAction::UserArgs)
        try {
            run(bucket = arguments.bucket)
        }catch (e: S3Exception){
            println(e.message)
            return
        }
        println("The ${arguments.bucket} was successfully deleted")
    }
}