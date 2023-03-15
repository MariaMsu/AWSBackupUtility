package actions

import aws.sdk.kotlin.services.s3.model.S3Exception
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import utils.S3Interaction
import utils.ZipManager
import kotlin.io.path.*

object RestoreAction : Action {
    override val name = "restore"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "S3 bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val outDir by parser.storing(
            "-o", "--out-dir",
            help = "path to the restoring directory"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "the backup key (i.e. file name) in the S3 bucket"
        )
    }

    /**
     * download a compressed file from S3 and extract it
     */
    fun run(bucket: String, key: String, outDirStr: String) {
        val destDir = Path(outDirStr)
        if (destDir.exists() && destDir.parent.listDirectoryEntries().isNotEmpty()) {
            throw FileAlreadyExistsException(
                file = destDir.toFile(),
                reason = "the file ${destDir.pathString} already exist & is not empty"
            )
        }
        val tmpZipFile = createTempFile()
        S3Interaction.getObjectBytes(bucketName = bucket, keyName = key, zipOutputFile = tmpZipFile)
        ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = Path(outDirStr))
    }

    /**
    how to call from bash:
    $ restore -o /home/omar/Desktop/testDirOut -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>): Boolean {
        val arguments = ArgParser(commandArgs).parseInto(RestoreAction::UserArgs)
        try {
            run(bucket = arguments.bucket, key = arguments.key, outDirStr = arguments.outDir)
        } catch (e: S3Exception) {
            println("The bucket with the name '${arguments.bucket}' or key '${arguments.key}' does not exist")
            return false
        } catch (e: FileAlreadyExistsException) {
            println(e.reason)
            return false
        }
        println("Successfully read ${arguments.key} from ${arguments.bucket} and wrote to ${arguments.outDir}")
        return true
    }
}
