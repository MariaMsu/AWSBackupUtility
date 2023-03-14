package actions

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
            help = "name of the backup file in the S3 bucket"
        )
    }

    /**
     * download a compressed file from S3 and extract it
     */
    fun run(bucket: String, key: String, outDirStr: String) {
        val tmpZipFile = createTempFile()
        S3Interaction.getObjectBytes(
            bucketName = bucket,
            keyName = key,
            zipOutputFile = tmpZipFile
        )
        ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = Path( outDirStr))
    }

    /**
    how to call from bash:
    $ restore -o /home/omar/Desktop/testDirOut -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(RestoreAction::UserArgs)
        run(bucket = arguments.bucket, key = arguments.key, outDirStr = arguments.outDir)
        println("Successfully read ${arguments.key} from ${arguments.bucket} and wrote to ${arguments.outDir}")
    }
}