import actions.ListBucketAction
import aws.sdk.kotlin.services.s3.model.NoSuchBucket
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Path
import kotlin.io.path.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActionsTest {

    val bucket = "backup-jetbrains-test"
    private var tmpDir: Path = createTempDirectory(prefix = "tmpTestDir")

    /**
    creates test data
    tmpTestDir
    └── localData
        ├── nestedDir
        │   └── file.txt
        └── ха-ха-ыыы.txt  // not ascii symbols
     **/
    @BeforeAll
    fun createTestLocalData() {
        tmpDir = createTempDirectory(prefix = "tmpTestDir")
        println("Temporary test data is located in $tmpDir")

        Path(base = tmpDir.pathString, subpaths = arrayOf("localData")).createDirectories()
        Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "ха-ха-ыыы.txt"))
            .createFile().writeText("Гамарджоба, генацвале!")  // not ascii symbols
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

    }
}