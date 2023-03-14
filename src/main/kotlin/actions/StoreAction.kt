package actions

import Defaults
import S3Interaction
import ZipManager
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

object StoreAction: Action {
    override val name = "store"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val inDir by parser.storing(
            "-i", "--in-dir",
            help = "path to the storing directory"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in S3"
        )
    }

    fun run(bucketName: String, objectKey: String, inDir: String) {
        val zipFile = ZipManager.zipFolderIntoTmp(File(inDir))  // TODO close files
        S3Interaction.putS3Object(
            bucketName = bucketName,
            objectKey = objectKey,
            objectPath = zipFile.path
        )
    }

    /**
     example: store -i /home/omar/Desktop/testDir -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(StoreAction::UserArgs)
        run(bucketName = arguments.bucket, objectKey = arguments.key, inDir = arguments.inDir)
    }
}
