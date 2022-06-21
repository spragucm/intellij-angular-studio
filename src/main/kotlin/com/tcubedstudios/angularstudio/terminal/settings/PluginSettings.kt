package com.tcubedstudios.angularstudio.terminal.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.tcubedstudios.angularstudio.terminal.settings.PluginSettings",
    storages = [Storage("nativeTerminalPlugin.xml")]
)
class PluginSettings: PersistentStateComponent<PluginSettingsState> {

    companion object {
        fun getInstance(): PluginSettings = ServiceManager.getService(PluginSettings::class.java)
    }

    private var pluginSettingsState: PluginSettingsState = PluginSettingsState()

    override fun getState(): PluginSettingsState  = pluginSettingsState

    override fun loadState(pluginSettingsState: PluginSettingsState) {
        this.pluginSettingsState = pluginSettingsState
    }
}