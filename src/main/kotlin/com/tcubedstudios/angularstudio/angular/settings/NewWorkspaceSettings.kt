package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.components.*

@State(
    name = "NewWorkspaceSettings",
    storages = [Storage("newWorkspaceSettings.xml")]//Do not use: Storage(StoragePathMacros.WORKSPACE_FILE)
)
class NewWorkspaceSettings: PersistentStateComponent<NewWorkspaceSettingsState> {

    companion object {
        fun getInstance(): NewWorkspaceSettings = ServiceManager.getService(NewWorkspaceSettings::class.java)
    }

    private var state = NewWorkspaceSettingsState()

    override fun getState(): NewWorkspaceSettingsState {
        return state
    }

    override fun loadState(state: NewWorkspaceSettingsState) {
        this.state = state
    }
}