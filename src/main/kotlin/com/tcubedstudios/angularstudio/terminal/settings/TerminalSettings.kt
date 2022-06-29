package com.tcubedstudios.angularstudio.terminal.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.tcubedstudios.angularstudio.terminal.settings.PluginSettings",
    storages = [Storage("nativeTerminalPlugin.xml")]
)
class TerminalSettings: PersistentStateComponent<TerminalSettingsState> {

    companion object {
        fun getInstance(): TerminalSettings = ServiceManager.getService(TerminalSettings::class.java)
    }

    private var terminalSettingsState: TerminalSettingsState = TerminalSettingsState()

    override fun getState(): TerminalSettingsState  = terminalSettingsState

    override fun loadState(terminalSettingsState: TerminalSettingsState) {
        this.terminalSettingsState = terminalSettingsState
    }
}