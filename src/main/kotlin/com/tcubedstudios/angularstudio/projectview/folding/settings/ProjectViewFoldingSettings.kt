package com.tcubedstudios.angularstudio.projectview.folding.settings

import com.intellij.openapi.components.*

@State(
    name = "ProjectViewFoldingSettings",
    storages = [Storage("projectViewFoldingSettings.xml")]
)
class ProjectViewFoldingSettings: PersistentStateComponent<ProjectViewFoldingSettingsState> {
    companion object {
        fun getInstance(): ProjectViewFoldingSettings = ServiceManager.getService(ProjectViewFoldingSettings::class.java)
    }

    private var state = ProjectViewFoldingSettingsState()

    override fun getState(): ProjectViewFoldingSettingsState {
        return state
    }

    override fun loadState(state: ProjectViewFoldingSettingsState) {
        this.state = state
    }
}