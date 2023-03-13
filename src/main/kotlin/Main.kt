// snippet-start:[s3.kotlin.create_bucket.import]
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File


//import kotlin.system.exitProcess
// snippet-end:[s3.kotlin.create_bucket.import]


class MyArgs(parser: ArgParser) {
    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose mode").default("verbose")

    val name by parser.storing(
        "-N", "--name",
        help = "name of the user").default("John Doe")

    val count by parser.storing(
        "-c", "--count",
        help = "number of widgets") { toInt() }.default("1")

    val source by parser.positional(
        "SOURCE",
        help = "source filename").default("file/")

    val destination by parser.positional(
        "DEST",
        help = "destination filename").default("new_file/")
}


/**
Before running this Kotlin code example, set up your development environment,
including your credentials.
For more information, see the following documentation topic:
https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
 */
suspend fun main(args: Array<String>) {
    ArgParser(args).parseInto(::MyArgs).run {
        println("Hello, ${name}!")
        println("I'm going to move ${count} widgets from ${source} to ${destination}.")
        // TODO: move widgets
    }

    val usage = """
    Usage:
        <bucketName> 
    Where:
        bucketName - The name of the Amazon S3 bucket to create. The Amazon S3 bucket name must be unique, or an error occurs.
    """

//    if (args.size != 1) {
//        println(usage)
//        exitProcess(0)
//    }
//    val bucketName = args[0]
    val inputDir = File("/home/omar/Desktop/leetcode")
    val outDirPath = "/home/omar/Desktop/leetcode_out"
    val zipFile = ZipManager.zipFolderIntoTmp(inputDir)
    ZipManager.unzip(zipFilePath = zipFile, destDirectory = outDirPath)

    val bucketName = "test416"
    val objectKey = "1"
//    putS3Object(bucketName=bucketName, objectKey=objectKey, objectPath=zipFile.path)
//    deleteExistingBucket(bucketName)
}

