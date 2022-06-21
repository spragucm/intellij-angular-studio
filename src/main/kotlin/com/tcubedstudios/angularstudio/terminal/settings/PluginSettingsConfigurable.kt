package com.tcubedstudios.angularstudio.terminal.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.tcubedstudios.angularstudio.terminal.model.TerminalType
import com.tcubedstudios.angularstudio.terminal.model.TerminalType.GENERIC
import org.jdesktop.swingx.util.OS
import javax.swing.JComponent

class PluginSettingsConfigurable: Configurable {

    companion object {
        const val WARNING_MESSAGE = "The selected terminal currently is not supported and may not work properly"
    }

    // Set 'chooseFolders' depend on OS, because macOS application represents a directory.
    private val terminalChooserDescriptor: FileChooserDescriptor = FileChooserDescriptor(true, OS.isMacOSX(), false, false, false, false)

    private val project: Project
    private val pluginSettingsForm: PluginSettingsForm = PluginSettingsForm()
    private val pluginSettings: PluginSettings = PluginSettings.getInstance()

    private var selectedTerminal: VirtualFile? = null

    init {
        val projectManager = ProjectManager.getInstance()
        val openProjects = projectManager.openProjects
        project = if (openProjects.isNotEmpty()) openProjects[0] else projectManager.defaultProject

        // FileChooserDialog support - longforus
        val favoriteTerminal = pluginSettings.state.favoriteTerminal
        if (favoriteTerminal.isNotEmpty()) {
            selectedTerminal = VirtualFileManager.getInstance().findFileByUrl("file://$favoriteTerminal")
        }

        pluginSettingsForm.terminalFileChooserButton?.addActionListener {
            val chosenTerminals = FileChooserDialogImpl(terminalChooserDescriptor, project).choose(project, selectedTerminal)
            if (chosenTerminals.isNotEmpty()) {
                chosenTerminals[0]?.let { file ->
                    val canonicalPath = file.canonicalPath
                    val terminalType = TerminalType.getTerminalTypeFromFilename(canonicalPath)

                    if (terminalType == GENERIC) {
                        Messages.showWarningDialog(WARNING_MESSAGE, "WARNING")
                    }

                    selectedTerminal = file
                    pluginSettingsForm.favoriteTerminalField?.text = canonicalPath
                }
            }
        }
    }

    override fun getDisplayName(): String = "Native Terminal Plugin"

    override fun getHelpTopic(): String = "Configure Native Terminal Plugin"

    override fun createComponent(): JComponent? = pluginSettingsForm.settingsPanel

    override fun isModified(): Boolean = pluginSettingsForm.pluginSettingsState != pluginSettings.state

    override fun apply()  = pluginSettings.loadState(pluginSettingsForm.pluginSettingsState)

    override fun reset() {
        pluginSettingsForm.pluginSettingsState = pluginSettings.state
    }

    override fun disposeUIResources() { }
}