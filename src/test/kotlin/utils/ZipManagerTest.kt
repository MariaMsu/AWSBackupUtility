package utils

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import test_utils.LocalTestData
import java.util.*
import kotlin.io.path.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZipManagerTest {

    private var ltd = LocalTestData()
    private val zipFile = createTempFile()

    @AfterAll
    fun deleteTestLocalData() {
        ltd.deleteAllData()
        zipFile.deleteExisting()
    }

    @Test
    fun zipTest() {
        ZipManager.zipFolderIntoTmp(inputFileOrFolder = ltd.baseDir, outputZip = zipFile)
        ZipManager.unzip(zipFilePath = zipFile, destDirectory = ltd.baseOutDir)
        assertTrue(ltd.compareOriginAndOutDirs())
    }
}