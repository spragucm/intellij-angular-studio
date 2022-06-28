package com.tcubedstudios.angularstudio.util

import java.io.File
import java.io.InputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.URL
import java.net.MalformedURLException
import java.net.URI
import java.nio.file.CopyOption
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.Path

object FileUtils {
    fun getFileUrl(filePath: String): URL? {
        return try {
            FileUtils.javaClass.getResource(filePath)
        } catch(e: Exception) {
            null
        }
    }

    fun getFileInputStream(filePath: String): InputStream? {
        try {
            //Check for http URL
            try {
                return URL(filePath).openStream()
            } catch(_: MalformedURLException) {}

            // Check for local file
            try {
                return FileInputStream(filePath)
            } catch (_: FileNotFoundException) {}

            // Check for resource file
            try {
                return FileUtils.javaClass.getResourceAsStream(filePath)
            } catch(_: Exception) {}
        } catch(t: Throwable) {}

        return null
    }

    //  Reference: https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar/10308305#10308305
    fun copyFileFromJarToLocal(jarFilePath: String, localFilePath: String, copyOption: CopyOption = StandardCopyOption.REPLACE_EXISTING) {
        when (val jarFileInputStream = getFileInputStream(jarFilePath)) {
            null -> println("Could not copy $jarFilePath to $localFilePath")
            else -> {
                println("Copied $jarFilePath to $localFilePath")

                val jarFileUrl = getFileUrl(jarFilePath)
                val destinationFile = File(localFilePath)
                org.apache.commons.io.FileUtils.copyURLToFile(jarFileUrl, destinationFile)

                // TODO - Chris - would it be better to use Files.copy(InputStream, Path, CopyOption) in order to indicate copy approach?
            }
        }
    }
    // TODO - Chris - Add more file utils from
    //https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar/10308305#10308305
//    fun getFileFromJar(jarFilePath: String) {
//        val jarFileUri: URI = URI.create("jar:file:${file.toURI().getPath()}")
//        val fileSystem: FileSystem = FileSystems.newFileSystem(jarFileUri, env)
//    }

    // https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java?answertab=active#tab-top
    fun getWorkingDirectory(): String? = System.getProperty("user.dir")

    // https://www.baeldung.com/java-temp-directories
    fun getTempDirectory(): String? = System.getProperty("java.io.tmpdir")

    // https://www.baeldung.com/java-temp-directories
    fun createTempDirectory(prefix: String = "AngularStudio"): String {
        return Files.createTempDirectory(prefix).toFile().absolutePath
    }
}