package com.tcubedstudios.angularstudio.terminal.model

import java.io.File

enum class TerminalType(val command: String, val defaultPath: String) {
    // Windows
    CMDER("cmder", ""),
    WINDOWS_TERMINAL("wt", ""),
    COMMAND_PROMPT("cmd", ""),
    POWER_SHELL("powershell", ""),
    CON_EMU("conemu", ""),
    GIT_BASH("git-bash", ""),
    BASH("bash", ""),
    WSL("wsl", ""),

    // Linux
    GNOME_TERMINAL("gnome-terminal","/usr/bin/gnome-terminal"),
    KONSOLE("konsole", "/usr/bin/konsole"),
    RXVT("rxvt", ""),
    TERMINATOR("terminator", ""),
    KITTY("kitty", ""),

    // macOS
    MAC_TERMINAL("Terminal", ""),
    I_TERM("iTerm", ""),

    GENERIC("", "");

    companion object {
        fun getTerminalType(command: String): TerminalType {
            val lowerCommand = command.toLowerCase()
            values().forEach {
                if (it.command.toLowerCase() == lowerCommand) {
                    return it
                }
            }
            return GENERIC
        }

        fun getTerminalTypeFromFilename(fileName: String?): TerminalType {
            val lowerFileName = fileName?.toLowerCase()
            values().forEach {
                if (it != GENERIC && lowerFileName?.contains(it.command.toLowerCase()) == true) {
                    return it
                }
            }
            return GENERIC
        }

        fun getExecutableString(command: String): String {
            return File(command).name
        }
    }
}