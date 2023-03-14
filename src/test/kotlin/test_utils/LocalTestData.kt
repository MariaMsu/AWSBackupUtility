package test_utils

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

/**
 * create test data
 * tmpTestDir
 * └── localData
 *     ├── nestedDir
 *     │   └── file.txt
 *     └── ха-ха-ыыы.txt  // test not ascii symbols
 */
class LocalTestData {
    private var tmpDir: Path = createTempDirectory(prefix = "tmpTestDir")
    val baseDir = Path(base = tmpDir.pathString, subpaths = arrayOf("localData"))
    val file1 = Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "ха-ха-ыыы.txt"))
    val nestedDir = Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "nestedDir"))
    val file2 = Path(base = tmpDir.pathString, subpaths = arrayOf("localData", "nestedDir", "file.txt"))

    val baseOutDir = Path(base = tmpDir.pathString, subpaths = arrayOf("localDataOut"))

    init {
        println("Temporary test data is located in $tmpDir")

        baseDir.createDirectories()
        file1.createFile().writeText("Гамарджоба, генацвале!")  // test not ascii symbols
        nestedDir.createDirectories()
        file2.createFile().writeText("some\ntest\ntext")
    }

    @OptIn(ExperimentalPathApi::class)
    fun deleteAllData() {
        tmpDir.deleteRecursively()
    }

    /**
     * receive origin path and return a path to the corresponding file in the outDir
     */
    private fun origin2outPath(originPath: Path): Path {
        return Path(originPath.pathString.replace("localData", "localDataOut"))
    }


    /**
     * compare content of files in 2 folders
     */
    fun compareOriginAndOutDirs(): Boolean {
        return Files.readAllBytes(file1).contentEquals(Files.readAllBytes(origin2outPath(file1)))
    }
}
