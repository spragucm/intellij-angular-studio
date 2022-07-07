package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.components.*

@State(
    name = "NewWorkspaceSettings",
    storages = [Storage("newWorkspaceSettings.xml")]
)
class NewWorkspaceSettings: PersistentStateComponent<NewWorkspaceSettings> {//NewWorkspaceSettingsState

    var workspaceName: String = ""
        get() {
            val blabla = "ljkj"
            return field
        }
        set(value) {
            val blabblkj = "lkjlj"
            field = value
        }

    companion object {
        fun getInstance(): NewWorkspaceSettings = ServiceManager.getService(NewWorkspaceSettings::class.java)
    }

//    private var newWorkspaceSettingsState = NewWorkspaceSettingsState()

    override fun getState(): NewWorkspaceSettings = this//newWorkspaceSettingsState

    override fun loadState(newWorkspaceSettings: NewWorkspaceSettings) {
        this.workspaceName = newWorkspaceSettings.workspaceName
        //this.newWorkspaceSettingsState = newWorkspaceSettingsState
    }
}