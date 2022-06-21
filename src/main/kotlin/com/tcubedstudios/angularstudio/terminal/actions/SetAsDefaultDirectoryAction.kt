package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.ide.actions.RevealFileAction
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject
import com.tcubedstudios.angularstudio.terminal.utils.getSelectedDirectory
import com.tcubedstudios.angularstudio.terminal.utils.getSelectedFile


class SetAsDefaultDirectoryAction: DumbAwareAction() {
    companion object {
        val LOGGY = Logger.getInstance(SetAsDefaultDirectoryAction::class.java)
        const val DEFAULT_DIRECTORY_PROPERTY_KEY = "com.tcubedstudios.angularstudio.terminal.default_directory"
    }

    override fun update(event: AnActionEvent) {
        val project = event.getEventProject()
        event.presentation.isEnabledAndVisible = project != null && event.getSelectedFile() != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getEventProject() ?: return
        val directory = event.getSelectedDirectory() ?: return

        val properties = PropertiesComponent.getInstance(project)
        properties.setValue(DEFAULT_DIRECTORY_PROPERTY_KEY, directory.path)

        LOGGY.info("'${properties.getValue(DEFAULT_DIRECTORY_PROPERTY_KEY)}' is set as default directory for project:${project.name}")
    }
}