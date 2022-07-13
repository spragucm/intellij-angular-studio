package com.tcubedstudios.angularstudio.shared.util

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.tcubedstudios.angularstudio.cli.navigation.SwapOpenFileFromToAction
import com.tcubedstudios.angularstudio.shared.Direction
import com.tcubedstudios.angularstudio.shared.util.TabUtils.isToThe
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.*
import javax.swing.Icon


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

    fun copyFilesFromJarToLocal(jarStartingDirectoryPath: String, localDirectoryPath: String) {
        val jarFileUri = getFileUrl(jarStartingDirectoryPath)?.toURI()
        val jarAsFileSystem = FileSystems.newFileSystem(jarFileUri, mapOf<String,String>())
        val from = jarAsFileSystem.getPath("scripts")
        val to = Paths.get(localDirectoryPath)
        Files.walk(from).use { sources ->
            sources.forEach { src ->
                val destination: Path = to.resolve(from.relativize(src).toString())
                try {
                    val isDirectory = Files.isDirectory(destination)
                    when {
                        isDirectory && Files.notExists(destination) -> Files.createDirectories(destination)
                        !isDirectory -> Files.copy(src, destination, StandardCopyOption.REPLACE_EXISTING)
                        else -> {}
                    }
                } catch(e: Exception) {
                    println("e:$e")
                }
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
    fun FileEditorManagerEx.files(direction: Direction): List<VirtualFile> {
        return if (!hasOpenedFile()) {
            emptyList()
        } else {
            currentWindow.files.run {
                filter { indexOf(it) isToThe direction of indexOf(currentFile) }
            }
        }
    }

    fun getFileByPath(path: String): VirtualFile? = LocalFileSystem.getInstance().findFileByPath(path)

    fun swapOpenFileFromTo(project: Project, directoryPath: String, fileName: String, extensionToClose: String, extensionToOpen: String) {
        if (extensionToClose == extensionToOpen) return

        val filePath = "$directoryPath//$fileName"
        val fileToClosePath = "$filePath.$extensionToClose"
        val fileToOpenPath = "$filePath.$extensionToOpen"

        val fileToClose = getFileByPath(fileToClosePath) ?: return
        val fileToOpen = getFileByPath(fileToOpenPath) ?: return
        if (!fileToClose.exists() || !fileToOpen.exists()) return

        FileEditorManager.getInstance(project).closeFile(fileToClose)
        OpenFileDescriptor(project, fileToOpen).navigate(true)
    }
    fun getNextExtensionForFileName(directoryPath: String, fileName: String, currentExtension: String, extensions: List<String>): String {
        val currentIndex: Int = extensions.indexOf(currentExtension)
        if (currentIndex == -1) return currentExtension

        // Determine what file is next based on both the desired extensions and whether the file exists, given
        // that there may or may not be a file with the next desired extension.
        val basePath = "$directoryPath\\$fileName"
        for (i in SwapOpenFileFromToAction.extensions.indices) {
            val newIndex: Int = (currentIndex + 1 + i) % SwapOpenFileFromToAction.extensions.size
            val newExtension: String = SwapOpenFileFromToAction.extensions[newIndex]
            val newPath = "$basePath.$newExtension"
            val newFile = getFileByPath(newPath)
            if (newFile != null && newFile.exists()) return newExtension
        }
        return currentExtension
    }

    fun PsiFile.hasMatchingExtension(extensions: Map<String, Icon>): Boolean {
        return name.substringAfterLast(".").lowercase() in extensions
    }

    fun PsiFile.getNextSiblingWithExtension(extensions: Map<String, Icon>): PsiFile? {
        val name = name.substringBeforeLast(".")
        val siblings = parent?.files ?: return null

        return siblings.firstOrNull {
            it.name.substringBeforeLast(".") == name && it.hasMatchingExtension(extensions)
        }
    }

    fun PsiFile.fileManager(): FileEditorManagerEx = this.project.fileManager()

    fun Project.fileManager(): FileEditorManagerEx = FileEditorManagerEx.getInstanceEx(this)
}