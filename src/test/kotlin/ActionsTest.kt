import actions.ListBucketAction
import actions.StoreAction
import aws.sdk.kotlin.services.s3.model.NoSuchBucket
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActionsTest {

    private val bucket = "backup-jetbrains-test"
    private val s3keyDir = "backup-dir"
    private val s3keyFile = "backup-file"
    private var tmpDir = createTempDirectory(prefix = "tmpTestDir")

    /**
    creates test data
    tmpTestDir
    └── localData
        ├── nestedDir
        │   └── file.txt
        └── ха-ха-ыыы.txt  // test not ascii symbols
     **/
    @BeforeAll
    fun createTestLocalData() {
        tmpDir = createTempDirectory(prefix = "tmpTestDir")
        println("Temporary test data is located in $tmpDir")

        Path(base = tmpDir.pathString, subpaths = arrayOf("localData")).createDirectories()
        Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "ха-ха-ыыы.txt"))
            .createFile().writeText("Гамарджоба, генацвале!")  // test not ascii symbols
        Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "nestedDir")).createDirectories()
        Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "nestedDir", "file.txt"))
            .createFile().writeText("some\ntest\ntext")
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterAll
    fun deleteTestLocalData() {
        tmpDir.deleteRecursively()
    }

    @Test
    fun testActions() {
        assertThrows(NoSuchBucket::class.java) { ListBucketAction.run(bucketName = bucket)}

        val baseDir = Path(base = tmpDir.pathString, subpaths = arrayOf("localData"))
        val nestedFile =  Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "nestedDir", "file.txt"))
        StoreAction.run(bucket = bucket, key = s3keyFile, inDirStr = nestedFile.toString())
        StoreAction.run(bucket = bucket, key = s3keyDir, inDirStr = baseDir.toString())
    }
}