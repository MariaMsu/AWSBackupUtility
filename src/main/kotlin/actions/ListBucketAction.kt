package actions

import aws.sdk.kotlin.services.s3.model.NoSuchBucket
import aws.sdk.kotlin.services.s3.model.Object
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

object ListBucketAction : Action {
    override val name = "list-bucket"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)
    }

    fun run(bucketName: String): List<Object>? {
        return S3Interaction.listBucketObjects(bucketName = bucketName)
    }

    /**
     how to call from bash:
     $ list-bucket
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(::UserArgs)
        val contents: List<Object>?
        try {
            contents = run(bucketName = arguments.bucket)
        } catch (e: NoSuchBucket) {
            println("A bucket with the name '${arguments.bucket}' does not yet exist")
            return
        }
        if (contents != null) {
            contents.forEach { myObject ->
                println("The name of the key is ${myObject.key}")
                println("The object is ${calculateKb(myObject.size)} KBs")
                println("The owner is ${myObject.owner}")
            }
        } else {
            println("A bucket '${arguments.bucket}' is empty")
        }
    }

    private fun calculateKb(intValue: Long): Long {
        return intValue / 1024
    }
}