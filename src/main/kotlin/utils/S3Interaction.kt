package utils

import Defaults.S3_REGION
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToFile
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Path

// based on https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/kotlin/services/s3/src/main/kotlin/com/kotlin/s3
// API documentation is here https://sdk.amazonaws.com/kotlin/api/latest/s3/aws.sdk.kotlin.services.s3/index.html
object S3Interaction {

    fun listBucketObjects(bucketName: String): ListObjectsResponse {
        val request = ListObjectsRequest { bucket = bucketName }

        S3Client { region = S3_REGION }.use { s3 ->
            return runBlocking { s3.listObjects(request) }
        }
    }

    fun getObjectBytes(bucketName: String, keyName: String, zipOutputFile: Path) {
        val request = GetObjectRequest {
            key = keyName
            bucket = bucketName
        }

        S3Client { region = S3_REGION}.use { s3 ->
            runBlocking {
                s3.getObject(request) { resp ->
                    resp.body?.writeToFile(zipOutputFile)
                }
            }
        }
    }

    fun putS3Object(bucketName: String, objectKey: String, objectPath: String): PutObjectResponse {
        createBucketIfDoesNotExist(bucketName = bucketName)

        val request = PutObjectRequest {
            bucket = bucketName
            key = objectKey
            body = File(objectPath).asByteStream()
        }


        S3Client { region = S3_REGION }.use { s3 ->
            return runBlocking { s3.putObject(request) }
        }
    }

    fun deleteBucketObjects(bucketName: String, objectName: String) {
        val objectId = ObjectIdentifier { key = objectName }
        val delOb = Delete { objects = listOf(objectId) }

        val request = DeleteObjectsRequest {
            bucket = bucketName
            delete = delOb
        }

        S3Client { region = S3_REGION }.use { s3 ->
            runBlocking { s3.deleteObjects(request) }
        }
    }

    fun createNewBucket(bucketName: String) {
        val request = CreateBucketRequest { bucket = bucketName }

        S3Client { region = S3_REGION }.use { s3 ->
            runBlocking { s3.createBucket(request) }
            println("$bucketName is ready")
        }
    }

    fun createBucketIfDoesNotExist(bucketName: String) {
        val request = HeadBucketRequest { bucket = bucketName }

        try {
            S3Client { region = S3_REGION }.use { s3 ->
                runBlocking { s3.headBucket(request) }
            }
        } catch (e: S3Exception) {
            println("A bucket with the name '$bucketName' does not yet exist")
            createNewBucket(bucketName)
        }
    }


    fun deleteExistingBucket(bucketName: String?) {
        val request = DeleteBucketRequest { bucket = bucketName }

        S3Client { region = S3_REGION }.use { s3 ->
            runBlocking { s3.deleteBucket(request) }
        }
    }
}