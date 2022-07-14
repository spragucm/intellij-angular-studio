package com.tcubedstudios.angularstudio.terminal.simpletons

import com.intellij.openapi.diagnostic.Logger
import java.io.File
import java.io.IOException

class Command(vararg cmds: String) {

    companion object {
        private val LOGGY = Logger.getInstance(Command::class.java)
    }

    val commands = cmds.asList().toMutableList()

    fun add(vararg cmds: String) = commands.addAll(cmds.asList())

    fun addAll(cmds: List<String>) = commands.addAll(cmds)

    fun execute(path: String) {
        try {
            ProcessBuilder(commands).directory(File(path)).start()
        } catch(e: IOException) {
            LOGGY.error("Could not execute commands:$commands path:$path e:$e")
        }
    }

    override fun toString(): String = "Command{commands=$commands}"
}