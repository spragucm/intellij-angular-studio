package com.tcubedstudios.angularstudio.terminal.utils

import com.intellij.openapi.diagnostic.Logger
import com.tcubedstudios.angularstudio.terminal.simpletons.Command
import com.tcubedstudios.angularstudio.terminal.simpletons.Environment
import com.tcubedstudios.angularstudio.terminal.simpletons.OperatingSystem
import com.tcubedstudios.angularstudio.terminal.simpletons.OperatingSystem.*
import com.tcubedstudios.angularstudio.terminal.simpletons.OperatingSystem.Companion.getDefaultTerminalType
import com.tcubedstudios.angularstudio.terminal.simpletons.OperatingSystem.UNKNOWN
import com.tcubedstudios.angularstudio.terminal.simpletons.TerminalType
import com.tcubedstudios.angularstudio.terminal.simpletons.TerminalType.*
import java.io.File
import java.util.regex.Pattern

object OperatingSystemUtils {

    val LOGGY = Logger.getInstance(OperatingSystemUtils::class.java)
    val OS_VERSION_PATTERN = Pattern.compile("^(\\d+\\.\\d+).*")
    val PROJECT_DIR_PLACEHOLDER = "\${project.dir}"

    fun parseOsVersion(osVersion: String): Double {
        try {
            val matcher = OS_VERSION_PATTERN.matcher(osVersion)
            if (matcher.matches()) {
                return matcher.group(1).toDouble()
            }
        } catch (e: Exception) {
            LOGGY.error("Failed to parse OS version:$osVersion e:$e")
        }
        return -1.0
    }

    fun toOperatingSystem(osName: String): OperatingSystem {
        return when (osName.substring(0, 3).toLowerCase()) {
            WINDOWS.osName -> WINDOWS
            LINUX.osName -> LINUX
            MAC_OS.osName -> MAC_OS
            else -> UNKNOWN
        }
    }

    fun createCommand(environment: Environment, projectDirectory: String, favoriteTerminal: String): Command? {
        if (!isDirectory(projectDirectory)) return null

        val command = favoriteTerminal.ifEmpty {
            val gui = environment.gui
            val defaultTerminalType = environment.operatingSystem.getDefaultTerminalType(gui)
            defaultTerminalType.command
        }

        if (isCustomCommand(command)) {
            val commands = command
                .replace(PROJECT_DIR_PLACEHOLDER, projectDirectory)
                .split(" ".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()

            return Command().apply { addAll(commands.toList()) }
        }

        val terminalType = TerminalType.getTerminalType(command)
        LOGGY.info("Favorite terminal is [$favoriteTerminal] and using[$terminalType]")
        LOGGY.info("Project directory:$projectDirectory")

        return when (val operatingSystem = Environment.operatingSystem) {
            WINDOWS -> {
                when(terminalType) {
                    COMMAND_PROMPT -> Command("cmd", "/c", "start", command, "/K", "cd", "/d", projectDirectory)
                    POWER_SHELL -> Command("cmd", "/c", "start", command, "-NoExit", "-Command", "Set-Location", "'$projectDirectory'")
                    WINDOWS_TERMINAL -> Command(command, "-d", projectDirectory)
                    CMDER -> Command(command, "/task", "cmder", projectDirectory)
                    GIT_BASH -> Command(command, "--cd=$projectDirectory")
                    WSL,
                    BASH -> Command("cmd", "/k", "start", "/d", projectDirectory.replace("/", "\\"), command)
                    CON_EMU -> {
                        val conEmuRunCommand = " -run "
                        val commands = command.split(conEmuRunCommand.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val executableCommand = Command(commands[0], "-Dir", projectDirectory)

                        if (commands.size == 2) {
                            executableCommand.add("-run", commands[1])
                        }
                        executableCommand
                    }
                    else -> Command("cmd", "/c", "start", command)
                }
            }
            LINUX -> {
                when(terminalType) {
                    GNOME_TERMINAL -> Command(command, "--working-directory", projectDirectory)
                    KONSOLE -> Command(command, "--new-tab", "--workdir", projectDirectory)
                    TERMINATOR -> Command(command, "--new-tab", "--working-directory", projectDirectory)
                    KITTY -> Command(command, "-1", "-d", projectDirectory)
                    RXVT -> Command(command, "-cd", projectDirectory)
                    else -> Command(command)
                }
            }
            MAC_OS -> {
                Command("open", projectDirectory, "-a", command)
            }
            else -> {
                LOGGY.error("Cannot create operating system ($operatingSystem) invalid. Can't create command:$command")
                null
            }
        }
    }

    fun isCustomCommand(command: String): Boolean = command.contains(PROJECT_DIR_PLACEHOLDER)

    fun isDirectory(path: String): Boolean {
        val file = File(path)
        return when {
            !file.exists() || !file.isDirectory -> false
            else -> true
        }
    }
}