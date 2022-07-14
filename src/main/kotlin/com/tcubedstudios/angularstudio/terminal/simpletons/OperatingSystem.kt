package com.tcubedstudios.angularstudio.terminal.simpletons

import java.io.File
import com.tcubedstudios.angularstudio.terminal.simpletons.TerminalType.*

enum class OperatingSystem(val osName: String) {
    WINDOWS("win"),
    LINUX("lin"),
    MAC_OS("mac"),
    UNKNOWN("unk");

    companion object {
        fun OperatingSystem.getDefaultTerminalType(gui: String): TerminalType {
            return when {
                this == WINDOWS -> COMMAND_PROMPT
                this == MAC_OS -> MAC_TERMINAL
                this == LINUX && (gui.toLowerCase().contains("gnome") || File(GNOME_TERMINAL.defaultPath).exists()) -> GNOME_TERMINAL
                this == LINUX -> KONSOLE
                else -> TerminalType.GENERIC
            }
        }
    }
}