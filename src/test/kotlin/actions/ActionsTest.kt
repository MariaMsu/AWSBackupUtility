package actions

import aws.sdk.kotlin.services.s3.model.S3Exception
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import test_utils.LocalTestData
import kotlin.io.path.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActionsTest {

    private val bucket = "backup-jetbrains-test"
    private val s3keyDir = "backup-dir"
    private val s3keyFile = "backup-file"
    private var ltd = LocalTestData()
    private val zipFile = createTempFile()
    private val fileOutDir = createTempDirectory()


    @AfterAll
    fun deleteTestLocalData() {
        ltd.deleteAllData()
        zipFile.deleteExisting()
        fileOutDir.deleteExisting()
    }

    @Test
    fun testActions() {
//        assertThrows(NoSuchBucket::class.java) { ListBucketAction.run(bucket = bucket) }

        StoreAction.run(bucket = bucket, key = s3keyFile, inDirStr = ltd.file2.toString())
        StoreAction.run(bucket = bucket, key = s3keyDir, inDirStr = ltd.baseDir.toString())

        val listResponse = ListBucketAction.run(bucket = bucket)
        assertEquals(2, listResponse.contents!!.size)

        val expectedKeySet = setOf<String>(s3keyDir, s3keyFile)
        val actualKeySet = setOf<String>(listResponse.contents!![0].key!!, listResponse.contents!![1].key!!)
        assertEquals(expectedKeySet, actualKeySet)

        // this bucket does not exist
        assertThrows(S3Exception::class.java) {
            RestoreAction.run(
                bucket = bucket + "123", key = s3keyDir, outDirStr = ltd.baseOutDir.pathString
            )
        }
        // this key does not exist
        assertThrows(S3Exception::class.java) {
            RestoreAction.run(
                bucket = bucket, key = s3keyDir + "123", outDirStr = ltd.baseOutDir.pathString
            )
        }
        RestoreAction.run(bucket = bucket, key = s3keyDir, outDirStr = ltd.baseOutDir.pathString)
        assertTrue(ltd.compareOriginAndOutDirs())
        // this file already exist
        assertThrows(FileAlreadyExistsException::class.java) {
            RestoreAction.run(
                bucket = bucket, key = s3keyDir, outDirStr = ltd.baseOutDir.pathString
            )
        }

        // try to extract from the backup a not existing file
        assertThrows(NoSuchFileException::class.java) {
            RestoreFileAction.run(
                bucket = bucket,
                key = s3keyDir,
                outDirStr = fileOutDir.pathString,
                fileStr = "some/not/existing/path"
            )
        }
        val relativePath = ltd.file2.relativeTo(ltd.baseDir)
        RestoreFileAction.run(
            bucket = bucket, key = s3keyDir, outDirStr = fileOutDir.pathString, fileStr = relativePath.pathString
        )


    }
}