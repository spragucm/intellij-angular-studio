package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.tcubedstudios.angularstudio.terminal.settings.PluginSettingsState
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject
import com.tcubedstudios.angularstudio.terminal.utils.getSelectedDirectory
import com.tcubedstudios.angularstudio.terminal.utils.getSelectedFile

class OpenSelectedDirectoryAction: OpenTerminalBaseAction() {

    override fun update(event: AnActionEvent) {
        val project = event.getEventProject()
        event.presentation.isEnabledAndVisible = project != null && event.getSelectedFile() != null
    }

    override fun getDirectory(event: AnActionEvent, settings: PluginSettingsState): String {
        return when (val directory = event.getSelectedDirectory()) {
            null -> System.getProperty("user.home")
            else -> directory.path
        }
    }
}