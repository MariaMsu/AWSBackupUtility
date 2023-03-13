import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import java.io.File

// source: https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/kotlin/services/s3/src/main/kotlin/com/kotlin/s3
object S3Interaction {

    // snippet-start:[s3.kotlin.list_objects.main]
    suspend fun listBucketObjects(bucketName: String) {

        val request = ListObjectsRequest {
            bucket = bucketName
        }

        S3Client { region = "us-east-1" }.use { s3 ->

            val response = s3.listObjects(request)
            response.contents?.forEach { myObject ->
                println("The name of the key is ${myObject.key}")
                println("The object is ${calKb(myObject.size)} KBs")
                println("The owner is ${myObject.owner}")
            }
        }
    }

    private fun calKb(intValue: Long): Long {
        return intValue / 1024
    }
// snippet-end:[s3.kotlin.list_objects.main]


    suspend fun getObjectBytes(bucketName: String, keyName: String, path: String) {

        val request = GetObjectRequest {
            key = keyName
            bucket = bucketName
        }

        S3Client { region = "us-east-1" }.use { s3 ->
            s3.getObject(request) { resp ->
                val myFile = File(path)
                resp.body?.writeToFile(myFile)
                println("Successfully read $keyName from $bucketName")
            }
        }
    }

    suspend fun putS3Object(bucketName: String, objectKey: String, objectPath: String) {

        val metadataVal = mutableMapOf<String, String>()
        metadataVal["myVal"] = "test"

        val request = PutObjectRequest {
            bucket = bucketName
            key = objectKey
            metadata = metadataVal
            body = File(objectPath).asByteStream()
        }

        S3Client { region = "us-east-1" }.use { s3 ->
            val response = s3.putObject(request)
            println("Tag information is ${response.eTag}")
        }
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
}