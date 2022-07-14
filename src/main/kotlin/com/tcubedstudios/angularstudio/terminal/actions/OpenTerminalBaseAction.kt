package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.diagnostic.Logger
import com.tcubedstudios.angularstudio.terminal.simpletons.Environment
import com.tcubedstudios.angularstudio.terminal.settings.TerminalSettings
import com.tcubedstudios.angularstudio.terminal.settings.TerminalSettingsState
import com.tcubedstudios.angularstudio.terminal.utils.OperatingSystemUtils
import java.io.IOException

abstract class OpenTerminalBaseAction: DumbAwareAction() {
    companion object {
        val LOGGY = Logger.getInstance(OpenTerminalBaseAction::class.java)
    }

    abstract fun getDirectory(event: AnActionEvent, settings: TerminalSettingsState): String

    override fun actionPerformed(event: AnActionEvent) {
        LOGGY.info(Environment.toString())

        try {
            val terminalSettingsState = TerminalSettings.getInstance().getState()
            val directory = getDirectory(event, terminalSettingsState)
            val favoriteTerminal = terminalSettingsState.favoriteTerminal

            when(val command = OperatingSystemUtils.createCommand(Environment, directory, favoriteTerminal)) {
                null -> LOGGY.error("Could not perform action, command is null")
                else -> {
                    LOGGY.info("Performing command:$command")
                    command.execute(directory)
                }
            }
        } catch(e: IOException) {
            LOGGY.error("Failed to execute the command! e:$e")
        }
    }
}