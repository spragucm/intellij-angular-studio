package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.ide.actions.RevealFileAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vcs.changes.ignore.actions.getSelectedFiles
import com.intellij.openapi.vfs.VirtualFile
import com.tcubedstudios.angularstudio.terminal.model.Environment
import com.tcubedstudios.angularstudio.terminal.settings.PluginSettings
import com.tcubedstudios.angularstudio.terminal.settings.PluginSettingsState
import com.tcubedstudios.angularstudio.terminal.utils.OperatingSystemUtils
import java.io.IOException

abstract class OpenTerminalBaseAction: DumbAwareAction() {
    companion object {
        val LOGGY = Logger.getInstance(OpenTerminalBaseAction::class.java)
    }

    abstract fun getDirectory(event: AnActionEvent, settings: PluginSettingsState): String

    override fun actionPerformed(event: AnActionEvent) {
        LOGGY.info(Environment.toString())

        try {
            val pluginSettingsState = PluginSettings.getInstance().getState()
            val directory = getDirectory(event, pluginSettingsState)
            val favoriteTerminal = pluginSettingsState.favoriteTerminal

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