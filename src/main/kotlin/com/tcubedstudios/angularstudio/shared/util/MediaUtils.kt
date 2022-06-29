package com.tcubedstudios.angularstudio.shared.util

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

object MediaUtils {
    val EXTERNAL_BUFFER_SIZE =  128000

    fun playAudioFile(filePath: String) {
        val audioInputStream: AudioInputStream
        try {
            val fileInputStream = FileUtils.getFileInputStream(filePath)
            audioInputStream = AudioSystem.getAudioInputStream(fileInputStream)
        } catch(t: Throwable) {
            return
        }

        val audioFormat = audioInputStream.format
        val info = DataLine.Info(SourceDataLine::class.java, audioFormat)

        try {
            val line = AudioSystem.getLine(info) as? SourceDataLine ?: return
            line.open(audioFormat)
            line.start()

            var bytesRead = 0
            val data = ByteArray(EXTERNAL_BUFFER_SIZE)
            while(bytesRead != -1) {
                try {
                    bytesRead = audioInputStream.read(data, 0, data.size)
                } catch (t: Throwable) {
                    return
                }

                if(bytesRead >= 0) line.write(data, 0, bytesRead)
            }

            line.drain()
            line.close()
        } catch(t: Throwable) {
            return
        }
    }
}