package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.tcubedstudios.angularstudio.terminal.actions.SetAsDefaultDirectoryAction.Companion.DEFAULT_DIRECTORY_PROPERTY_KEY
import com.tcubedstudios.angularstudio.terminal.settings.PluginSettingsState
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject
import org.jetbrains.annotations.NotNull


class OpenProjectDirectoryAction: OpenTerminalBaseAction() {

    override fun getDirectory(event: AnActionEvent, settings: PluginSettingsState): String {
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