package utils

import java.io.*
import java.nio.file.Path

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

object ZipManager {

    /**
     * compress a file or a folder and write it to the outputZip file
     */
    @OptIn(ExperimentalPathApi::class)
    fun zipFolderIntoTmp(inputFileOrFolder: Path, outputZip: Path): Path {
        ZipOutputStream(BufferedOutputStream(outputZip.outputStream())).use { zos ->
            inputFileOrFolder.walk().forEach { file ->
                val zipFileName =
                    file.absolutePathString().removePrefix(inputFileOrFolder.absolutePathString()).removePrefix("/")
                val entry = ZipEntry("$zipFileName${(if (file.isDirectory()) "/" else "")}")
                zos.putNextEntry(entry)
                if (file.isRegularFile()) {
                    file.inputStream().use { fis -> fis.copyTo(zos) }
                }
            }
        }
        return outputZip
    }

    /**
     * extract a file or a folder and write it to the destDirectory
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: Path, destDirectory: Path) {

        if (!destDirectory.exists()) {
            destDirectory.createDirectories()
        }

        ZipFile(zipFilePath.toFile()).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val filePath =  Path(base = destDirectory.pathString, subpaths = arrayOf(entry.name))

                    if (entry.isDirectory) {
                        // if the entry is a directory, make the directory
                        filePath.createDirectories()
                        println("!!! create dir: ${filePath}")
                    } else {
                        // if the entry is a file, extracts it
                        filePath.parent.createDirectories()
                        filePath.createFile()
                        println("!!! create file: ${filePath}")
                        extractFile(input, filePath.pathString)
                    }
                }
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
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