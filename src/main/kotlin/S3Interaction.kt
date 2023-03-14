import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.zip.ZipFile

// based on https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/kotlin/services/s3/src/main/kotlin/com/kotlin/s3
object S3Interaction {

    fun listBucketObjects(bucketName: String) {
        val request = ListObjectsRequest { bucket = bucketName }

        S3Client { region = "us-east-1" }.use { s3 ->
            val response = runBlocking { s3.listObjects(request) }
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


    fun getObjectBytes(bucketName: String, keyName: String, zipOutputFile: File) {
        val request = GetObjectRequest {
            key = keyName
            bucket = bucketName
        }

        S3Client { region = "us-east-1" }.use { s3 ->
            runBlocking {
                s3.getObject(request) { resp ->
                    resp.body?.writeToFile(zipOutputFile)
                    println("Successfully read $keyName from $bucketName")
                }
            }
        }
    }

    fun putS3Object(bucketName: String, objectKey: String, objectPath: String) {
        createBucketIfDoesNotExist(bucketName = bucketName)

        val request = PutObjectRequest {
            bucket = bucketName
            key = objectKey
            body = File(objectPath).asByteStream()
        }

        S3Client { region = "us-east-1" }.use { s3 ->
            val response = runBlocking { s3.putObject(request) }
            println("Tag information is ${response.eTag}")
        }
    }

    fun deleteBucketObjects(bucketName: String, objectName: String) {
        val objectId = ObjectIdentifier { key = objectName }
        val delOb = Delete { objects = listOf(objectId) }

        val request = DeleteObjectsRequest {
            bucket = bucketName
            delete = delOb
        }

        S3Client { region = "us-east-1" }.use { s3 ->
            runBlocking { s3.deleteObjects(request) }
            println("$objectName was deleted from $bucketName")
        }
    }

    fun createNewBucket(bucketName: String) {
        val request = CreateBucketRequest { bucket = bucketName }

        S3Client { region = "us-east-1" }.use { s3 ->
            runBlocking { s3.createBucket(request) }
            println("$bucketName is ready")
        }
    }

    fun createBucketIfDoesNotExist(bucketName: String) {
        val request = HeadBucketRequest { bucket = bucketName }

        try {
            S3Client { region = "us-east-1" }.use { s3 ->
                runBlocking { s3.headBucket(request) }
            }
        } catch (e: S3Exception) {
            println("A bucket with the name '$bucketName' does not yet exist")
            createNewBucket(bucketName)
        }
    }


    fun deleteExistingBucket(bucketName: String?) {
        val request = DeleteBucketRequest { bucket = bucketName }

        S3Client { region = "us-east-1" }.use { s3 ->
            runBlocking { s3.deleteBucket(request) }
            println("The $bucketName was successfully deleted")
        }
    }
}