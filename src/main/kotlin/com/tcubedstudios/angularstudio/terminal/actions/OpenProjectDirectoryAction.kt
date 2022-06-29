package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.tcubedstudios.angularstudio.terminal.actions.SetAsDefaultDirectoryAction.Companion.DEFAULT_DIRECTORY_PROPERTY_KEY
import com.tcubedstudios.angularstudio.terminal.settings.TerminalSettingsState
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject


class OpenProjectDirectoryAction: OpenTerminalBaseAction() {

    override fun getDirectory(event: AnActionEvent, settings: TerminalSettingsState): String {
        val project = event.getEventProject() ?: return System.getProperty("user.home")

        val properties = PropertiesComponent.getInstance(project)
        var defaultDirectory = properties.getValue(DEFAULT_DIRECTORY_PROPERTY_KEY) ?: ""

        if (defaultDirectory.isEmpty()) {
            defaultDirectory = project.basePath ?: ""
            if (defaultDirectory.isEmpty()) {
                defaultDirectory = System.getProperty("user.home")
            }
        }
        return defaultDirectory
    }
}