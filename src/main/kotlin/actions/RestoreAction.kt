package actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

object RestoreAction : Action {
    override val name = "restore"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val outDir by parser.storing(
            "-o", "--out-dir",
            help = "path to the restoring directory"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in S3"
        )
    }

    fun run(bucketName: String, objectKey: String, outDir: String) {
        val tmpZipFile = File.createTempFile("out", "zip")
        S3Interaction.getObjectBytes(
            bucketName = bucketName,
            keyName = objectKey,
            zipOutputFile = tmpZipFile
        )
        ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = outDir)
    }

    /**
    example: restore -o /home/omar/Desktop/testDirOut -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(RestoreAction::UserArgs)
        run(bucketName = arguments.bucket, objectKey = arguments.key, outDir = arguments.outDir)
    }
}