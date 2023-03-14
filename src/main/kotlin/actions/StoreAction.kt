package actions

import Defaults
import utils.S3Interaction
import utils.ZipManager
import aws.sdk.kotlin.services.s3.model.PutObjectResponse
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.pathString

object StoreAction : Action {
    override val name = "store"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "S3 bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val inDir by parser.storing(
            "-i", "--in-dir",
            help = "path to the storing directory"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in the S3 bucket"
        )
    }

    /**
     * Compress a folder or a file to a zip archive and put it to S3
     */
    fun run(bucket: String, key: String, inDirStr: String): PutObjectResponse {
        val zipFile = kotlin.io.path.createTempFile()
        ZipManager.zipFolderIntoTmp(inputFileOrFolder = Path(inDirStr), outputZip = zipFile)
        val response = S3Interaction.putS3Object(
            bucketName = bucket, objectKey = key, objectPath = zipFile.pathString
        )
        zipFile.deleteExisting()
        return response
    }

    /**
    how to call from bash:
    $ store -i /home/omar/Desktop/testDir -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(StoreAction::UserArgs)
        val response = run(bucket = arguments.bucket, key = arguments.key, inDirStr = arguments.inDir)
        println("Tag information is ${response.eTag}")
    }
}
