import java.io.*
import java.nio.file.Path

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

object ZipManager {

    @OptIn(ExperimentalPathApi::class)
    fun zipFolderIntoTmp(inputDirectory: Path): Path {
        val outputZipFile = createTempFile()
        ZipOutputStream(BufferedOutputStream(outputZipFile.outputStream())).use { zos ->
            inputDirectory.walk().forEach { file ->
                val zipFileName = file.absolutePathString().removePrefix(inputDirectory.absolutePathString()).removePrefix("/")
                val entry = ZipEntry("$zipFileName${(if (file.isDirectory()) "/" else "")}")
                zos.putNextEntry(entry)
                if (file.isRegularFile()) {
                    file.inputStream().use { fis -> fis.copyTo(zos) }
                }
            }
        }
        return outputZipFile
    }

    /**
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: File, destDirectory: String) {

        File(destDirectory).run {
            if (!exists()) {
                mkdirs()
            }
        }

        ZipFile(zipFilePath).use { zip ->

            zip.entries().asSequence().forEach { entry ->

                zip.getInputStream(entry).use { input ->


                    val filePath = destDirectory + File.separator + entry.name

                    if (!entry.isDirectory) {
                        // if the entry is a file, extracts it
                        extractFile(input, filePath)
                    } else {
                        // if the entry is a directory, make the directory
                        val dir = File(filePath)
                        dir.mkdir()
                    }

                }

            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param inputStream
     * @param destFilePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    /**
     * Size of the buffer to read/write data
     */
    private const val BUFFER_SIZE = 4096

}