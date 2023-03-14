package actions

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import utils.S3Interaction
import utils.ZipManager
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.io.path.pathString

object RestoreFileAction : Action {
    override val name = "restore-file"

    private class UserArgs(parser: ArgParser) {
        val bucket by parser.storing(
            "-b", "--bucket",
            help = "S3 bucket name"
        ).default(Defaults.DEFAULT_BUCKET)

        val outDir by parser.storing(
            "-o", "--out-dir",
            help = "path to the restoring directory"
        )

        val filePath by parser.storing(
            "-f", "--file",
            help = "the file path in the stored directory"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in the S3 bucket"
        )
    }

    fun run(bucketName: String, objectKey: String, outDir: String, filePath: String) {
        val tmpZipFile = kotlin.io.path.createTempFile()
        S3Interaction.getObjectBytes(
            bucketName = bucketName,
            keyName = objectKey,
            zipOutputFile = tmpZipFile
        )
        val tmpDir = createTempDirectory(prefix = "tmpDir")
        ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = tmpDir)
        val tmpPath = Path(tmpDir.pathString, filePath)
        val newPath = Path(outDir, filePath)
        println(newPath.parent)
        if (!newPath.parent.exists()) {
            File(newPath.parent.pathString).mkdirs()   // todo fix it
        }
        Files.move(tmpPath, newPath)
    }

    /**
    example: restore-file -o /home/omar/Desktop/testDirSingleFile -f gradle/wrapper/gradle-wrapper.properties -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(RestoreFileAction::UserArgs)
        run(
            bucketName = arguments.bucket,
            objectKey = arguments.key,
            outDir = arguments.outDir,
            filePath = arguments.filePath
        )
    }
}