package actions

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString
class DeleteBackupActionTest {

    val bucket = "backup-jetbrains-test"

    /**
     creates test data
     tmpTestDir
     |-localData
       |-ха-ха-ыыы.txt
       |-nestedDir
         |-file.txt
     **/
    fun createTestLocalData(){
        val tmpDir = createTempDirectory(prefix = "tmpTestDir")
        println("Temporary teat data located in $tmpDir")
        tmpDir.pathString
        println((tmpDir + "aaa"))
//        File((tmpDir + "aaa").pathString).mkdirs()

//        File(Path(tmpDir.toString(), "nestedDir").pathString).mkdirs()
    }

    @Test
    fun testS3() {
        createTestLocalData()
        assertEquals(1 , 1)
        println("EEEEEEEEEEE")
    }
}