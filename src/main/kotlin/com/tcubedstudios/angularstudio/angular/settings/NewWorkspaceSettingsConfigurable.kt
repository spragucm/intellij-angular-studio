package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import javax.swing.JComponent

class NewWorkspaceSettingsConfigurable: Configurable {

    private val newWorkspaceSettingsForm: NewWorkspaceSettingsForm = NewWorkspaceSettingsForm()
    private val newWorkspaceSettings: NewWorkspaceSettings = NewWorkspaceSettings.getInstance()

    private var selectedDirectory: VirtualFile? = VirtualFileManager.getInstance().findFileByUrl("c:")

    override fun getDisplayName(): String = "Angular Studio New Workspace"

    override fun getHelpTopic(): String = "Configure Angular Studio New Workspace"

    init {
        newWorkspaceSettingsForm.workspacePathButton?.addActionListener {
            val projectManager = ProjectManager.getInstance()
            val project = projectManager.defaultProject
            val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
            val selectedDirectory = FileChooserDialogImpl(fileChooserDescriptor, project).choose(project, selectedDirectory)
        }
    }


    override fun createComponent(): JComponent? = newWorkspaceSettingsForm.settingsPanel

    override fun isModified(): Boolean = false//fixtures

    override fun apply() = newWorkspaceSettings.loadState(newWorkspaceSettingsForm.newWorkspaceSettingsState)

    override fun reset() {
        newWorkspaceSettingsForm.newWorkspaceSettingsState = newWorkspaceSettings.state
    }
}