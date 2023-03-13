// snippet-start:[s3.kotlin.create_bucket.import]
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File
import kotlin.system.exitProcess

// snippet-end:[s3.kotlin.create_bucket.import]

//restore --out-dir abc --bucket abc --key abc
//
//restore-file --out abc --bucket abc --key abc
//
//delete-backup --bucket abc --key abc
//
//delete-bucket --bucket abc
class ArgsListBucket(parser: ArgParser) {
    val bucket by parser.storing(
        "-b", "--bucket",
        help = "bucket name"
    ).default(Defaults.DEFAULT_BUCKET)
}

class ArgsStore(parser: ArgParser) {
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

class ArgsRestore(parser: ArgParser) {
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

/**
Before running this Kotlin code example, set up your development environment,
including your credentials.
For more information, see the following documentation topic:
https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
 */
fun main(args: Array<String>) {
//    println("args: ${args.joinToString(", ")}")

    val usage = """
    Usage:
        <bucketName> 
    Where:
        bucketName - The name of the Amazon S3 bucket to create. The Amazon S3 bucket name must be unique, or an error occurs.
    """

    if (args.isEmpty()) {
        println(usage)
        exitProcess(-1)
    }
    val command = args[0]
    val commandArgs = args.drop(1).toTypedArray()
    when (command) {
        "list-bucket" -> {
            val arguments = ArgParser(commandArgs).parseInto(::ArgsListBucket)
            S3Interaction.listBucketObjects(bucketName = arguments.bucket)
        }

        "store" -> {
            val arguments = ArgParser(commandArgs).parseInto(::ArgsStore)
            val zipFile = ZipManager.zipFolderIntoTmp(File(arguments.inDir))  // TODO close files
            S3Interaction.putS3Object(
                bucketName = arguments.bucket,
                objectKey = arguments.key,
                objectPath = zipFile.path
            )
        }

        "restore" -> {
            val arguments = ArgParser(commandArgs).parseInto(::ArgsRestore)
            val tmpZipFile = File.createTempFile("out", "zip")
            S3Interaction.getObjectBytes(
                bucketName = arguments.bucket,
                keyName = arguments.key,
                zipOutputFile = tmpZipFile
            )
            ZipManager.unzip(zipFilePath = tmpZipFile, destDirectory = arguments.outDir)
        }

        else -> { // Note the block
            print("x is neither 1 nor 2")
        }
    }


    val inputDir = File("/home/omar/Desktop/leetcode")
    val outDirPath = "/home/omar/Desktop/leetcode_out"
    val zipFile = ZipManager.zipFolderIntoTmp(inputDir)
    ZipManager.unzip(zipFilePath = zipFile, destDirectory = outDirPath)

    val bucketName = "test416"
    val objectKey = "1"
//    putS3Object(bucketName=bucketName, objectKey=objectKey, objectPath=zipFile.path)
//    deleteExistingBucket(bucketName)
}

