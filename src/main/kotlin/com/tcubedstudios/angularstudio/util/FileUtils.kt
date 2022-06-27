package com.tcubedstudios.angularstudio.util

import java.io.InputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.URL
import java.net.MalformedURLException

object FileUtils {
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
}