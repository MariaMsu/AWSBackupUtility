package actions

import aws.sdk.kotlin.services.s3.model.S3Exception
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import utils.S3Interaction
import utils.ZipManager
import java.nio.file.Files
import kotlin.io.path.*

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
            help = "the relative file path from the stored directory root"
        )

        val key by parser.storing(
            "-k", "--key",
            help = "name of the backup file in the S3 bucket"
        )
    }

    @OptIn(ExperimentalPathApi::class)
    fun run(bucket: String, key: String, outDirStr: String, fileStr: String) {
        val destDir = Path(outDirStr)
        if (destDir.exists() && destDir.listDirectoryEntries().isNotEmpty()) {
            throw FileAlreadyExistsException(
                file = destDir.toFile(),
                reason = "the file ${destDir.pathString} already exist & is not empty"
            )
        }
        val tmpZipFile = createTempFile()
        S3Interaction.getObjectBytes(bucketName = bucket, keyName = key, zipOutputFile = tmpZipFile)
        val tmpExtractDir = createTempDirectory()
        ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = tmpExtractDir)
        val tmpFilePath = Path(tmpExtractDir.pathString, subpaths = arrayOf(fileStr))
        if (!tmpFilePath.exists()) {
            throw NoSuchFileException(
                file = tmpFilePath.toFile(),
                reason = "the file ${tmpFilePath.pathString} does not exist in the '${key}' backup"
            )
        }
        val destFilePath = Path(outDirStr, fileStr)
        destFilePath.parent.createDirectories()
        Files.move(tmpFilePath, destFilePath)
        tmpExtractDir.deleteRecursively()
    }

    /**
    how to call from bash:
    restore-file -o /home/omar/Desktop/testDirSingleFile -f gradle/wrapper/gradle-wrapper.properties -k backupTest
     **/
    override fun parseArgsAndCall(commandArgs: Array<String>) {
        val arguments = ArgParser(commandArgs).parseInto(RestoreFileAction::UserArgs)
        try {
            run(
                bucket = arguments.bucket,
                key = arguments.key,
                outDirStr = arguments.outDir,
                fileStr = arguments.filePath
            )
        } catch (e: S3Exception) {
            println("The bucket with the name '${arguments.bucket}' or key '${arguments.key}' does not exist")
            return
        } catch (e: FileAlreadyExistsException) {
            println(e.reason)
            return
        } catch (e: NoSuchFileException) {
            println(e.reason)
            return
        }

        println("Successfully read ${arguments.key} from ${arguments.bucket} and wrote to ${arguments.outDir}")
    }
}