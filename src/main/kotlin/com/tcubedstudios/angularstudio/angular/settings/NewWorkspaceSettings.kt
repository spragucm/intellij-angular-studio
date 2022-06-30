package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.tcubedstudios.angularstudio.angular.settings.NewWorkspaceSettings",
    storages = [Storage("newWorkspaceSettings.xml")]
)
class NewWorkspaceSettings: PersistentStateComponent<NewWorkspaceSettingsState> {
    companion object {
        fun getInstance(): NewWorkspaceSettings = ServiceManager.getService(NewWorkspaceSettings::class.java)
    }

    private var newWorkspaceSettingsState = NewWorkspaceSettingsState()

    override fun getState(): NewWorkspaceSettingsState = newWorkspaceSettingsState

    override fun loadState(newWorkspaceSettingsState: NewWorkspaceSettingsState) {
        this.newWorkspaceSettingsState = newWorkspaceSettingsState
    }
}