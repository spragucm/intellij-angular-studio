package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class NewWorkspaceSettingsConfigurable: Configurable {

    private val newWorkspaceSettingsForm: NewWorkspaceSettingsForm = NewWorkspaceSettingsForm()
    private val newWorkspaceSettings: NewWorkspaceSettings = NewWorkspaceSettings.getInstance()

    override fun getDisplayName(): String = "Angular Studio New Workspace"

    override fun getHelpTopic(): String = "Configure Angular Studio New Workspace"

    override fun createComponent(): JComponent? = newWorkspaceSettingsForm.settingsPanel

    override fun isModified(): Boolean = false//fixtures

    override fun apply() = newWorkspaceSettings.loadState(newWorkspaceSettingsForm.newWorkspaceSettingsState)

    override fun reset() {
        newWorkspaceSettingsForm.newWorkspaceSettingsState = newWorkspaceSettings.state
    }
}