import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object ZipManager {

    fun zipFolderIntoTmp(inputDirectory: File): File {
        val outputZipFile = File.createTempFile("out", ".zip")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use {
                zos ->
            inputDirectory.walkTopDown().forEach { file ->
                val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
                val entry = ZipEntry("$zipFileName${(if (file.isDirectory) "/" else "")}")
                zos.putNextEntry(entry)
                if (file.isFile) {
                    file.inputStream().use { fis -> fis.copyTo(zos) }
                }
            }
        }
        return outputZipFile
    }
//    fun zip(files: List<File>, zipFile: File) {
//        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
//            files.forEach { file ->
//                (file.length() > 1).ifTrue {
//                    FileInputStream(file).use { input ->
//                        BufferedInputStream(input).use { origin ->
//                            val entry = ZipEntry(file.name.toRealName())
//                            output.putNextEntry(entry)
//                            origin.copyTo(output, 1024)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    //If we do not set encoding as "ISO-8859-1", European characters will be replaced with '?'.
//    fun unzip(files: List<File>, zipFile: ZipFile) {
//        zipFile.use { zip ->
//            zip.entries().asSequence().forEach { entry ->
//                zip.getInputStream(entry).use { input ->
//                    BufferedReader(InputStreamReader(input, "ISO-8859-1")).use { reader ->
//                        files.find { it.name.contains(entry.name) }?.run {
//                            BufferedWriter(FileWriter(this)).use { writer ->
//                                var line: String? = null
//                                while ({ line = reader.readLine(); line }() != null) {
//                                    writer.append(line).append('\n')
//                                }
//                                writer.flush()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    fun unzipFolder(inputZipFile: ZipFile, outputDirectory: File): File {
//        inputZipFile.use {
//                zos ->
//            inputDirectory.walkTopDown().forEach { file ->
//                val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath).removePrefix("/")
//                val entry = ZipEntry( "$zipFileName${(if (file.isDirectory) "/" else "" )}")
//                zos.putNextEntry(entry)
//                if (file.isFile) {
//                    file.inputStream().use { fis -> fis.copyTo(zos) }
//                }
//            }
//        }
//        return outputZipFile
//    }

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