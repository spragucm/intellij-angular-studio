package com.tcubedstudios.angularstudio.terminal.settings

import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

// This is bound to PluginSettingsForm.form, which means views could possibly be null
class PluginSettingsForm {
    var settingsPanel: JPanel? = null

    var favoriteTerminalField: JTextField? = null

    var terminalFileChooserButton: JButton? = null

    var pluginSettingsState: PluginSettingsState = PluginSettingsState()
        set(value) {
            field = value
            favoriteTerminalField?.text = value.favoriteTerminal
        }
}