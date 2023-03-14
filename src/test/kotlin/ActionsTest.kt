import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.nio.file.Path
import kotlin.io.path.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActionsTest {

    val bucket = "backup-jetbrains-test"
    private var tmpDir: Path? = null

    /**
    creates test data
    tmpTestDir
    └── localData
        ├── nestedDir
        │   └── file.txt
        └── ха-ха-ыыы.txt
     **/
    @BeforeAll
    fun createTestLocalData() {
        tmpDir = createTempDirectory(prefix = "tmpTestDir")
        println("Temporary teat data located in $tmpDir")

        Path(base = tmpDir!!.pathString, subpaths = arrayOf("localData")).createDirectories()
        Path(base = tmpDir!!.pathString, subpaths = arrayOf("localData", "ха-ха-ыыы.txt"))
            .createFile().writeText("Гамарджоба, генацвале!")  // not ascii symbols
        Path(base = tmpDir!!.pathString, subpaths = arrayOf("localData", "nestedDir")).createDirectories()
        Path(base = tmpDir!!.pathString, subpaths = arrayOf("localData", "nestedDir", "file.txt"))
            .createFile().writeText("some\ntest\ntext")
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterAll
    fun deleteTestLocalData(){
        tmpDir?.deleteRecursively()
    }

    @Test
    fun testS3() {
        assertEquals(1, 1)
        println("EEEEEEEEEEE")
    }
}