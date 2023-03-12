// snippet-start:[s3.kotlin.create_bucket.import]
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.CreateBucketRequest
import aws.sdk.kotlin.services.s3.model.DeleteBucketRequest
//import kotlin.system.exitProcess
// snippet-end:[s3.kotlin.create_bucket.import]

/**
Before running this Kotlin code example, set up your development environment,
including your credentials.
For more information, see the following documentation topic:
https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
 */
suspend fun main(args: Array<String>) {

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
    val bucketName = "test412"
    deleteExistingBucket(bucketName)
}

// snippet-start:[s3.kotlin.create_bucket.main]
suspend fun createNewBucket(bucketName: String) {

    val request = CreateBucketRequest {
        bucket = bucketName
    }

    S3Client { region = "us-east-1" }.use { s3 ->
        s3.createBucket(request)
        println("$bucketName is ready")
    }
}
// snippet-end:[s3.kotlin.create_bucket.main]


// snippet-start:[s3.kotlin.del_bucket.main]
suspend fun deleteExistingBucket(bucketName: String?) {

    val request = DeleteBucketRequest {
        bucket = bucketName
    }
    S3Client { region = "us-east-1" }.use { s3 ->
        s3.deleteBucket(request)
        println("The $bucketName was successfully deleted!")
    }
}
// snippet-end:[s3.kotlin.del_bucket.main]
