package com.tcubedstudios.angularstudio.terminal.settings

import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

// This is bound to TerminalSettingsForm.form, which means views could possibly be null
class TerminalSettingsForm {
    var settingsPanel: JPanel? = null

    var favoriteTerminalField: JTextField? = null

    var terminalFileChooserButton: JButton? = null

    var terminalSettingsState: TerminalSettingsState = TerminalSettingsState()
        set(value) {
            field = value
            favoriteTerminalField?.text = value.favoriteTerminal
        }
}