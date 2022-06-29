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

class TerminalSettingsConfigurable: Configurable {

    companion object {
        const val WARNING_MESSAGE = "The selected terminal currently is not supported and may not work properly"
    }

    // Set 'chooseFolders' depend on OS, because macOS application represents a directory.
    private val terminalChooserDescriptor: FileChooserDescriptor = FileChooserDescriptor(true, OS.isMacOSX(), false, false, false, false)

    private val project: Project
    private val terminalSettingsForm: TerminalSettingsForm = TerminalSettingsForm()
    private val terminalSettings: TerminalSettings = TerminalSettings.getInstance()

    private var selectedTerminal: VirtualFile? = null

    init {
        val projectManager = ProjectManager.getInstance()
        val openProjects = projectManager.openProjects
        project = if (openProjects.isNotEmpty()) openProjects[0] else projectManager.defaultProject

        // FileChooserDialog support - longforus
        val favoriteTerminal = terminalSettings.state.favoriteTerminal
        if (favoriteTerminal.isNotEmpty()) {
            selectedTerminal = VirtualFileManager.getInstance().findFileByUrl("file://$favoriteTerminal")
        }

        terminalSettingsForm.terminalFileChooserButton?.addActionListener {
            val chosenTerminals = FileChooserDialogImpl(terminalChooserDescriptor, project).choose(project, selectedTerminal)
            if (chosenTerminals.isNotEmpty()) {
                chosenTerminals[0]?.let { file ->
                    val canonicalPath = file.canonicalPath
                    val terminalType = TerminalType.getTerminalTypeFromFilename(canonicalPath)

                    if (terminalType == GENERIC) {
                        Messages.showWarningDialog(WARNING_MESSAGE, "WARNING")
                    }

                    selectedTerminal = file
                    terminalSettingsForm.favoriteTerminalField?.text = canonicalPath
                }
            }
        }
    }

    override fun getDisplayName(): String = "Angular Studio Terminal"

    override fun getHelpTopic(): String = "Configure Angular Studio Terminal"

    override fun createComponent(): JComponent? = terminalSettingsForm.settingsPanel

    override fun isModified(): Boolean = terminalSettingsForm.terminalSettingsState != terminalSettings.state

    override fun apply()  = terminalSettings.loadState(terminalSettingsForm.terminalSettingsState)

    override fun reset() {
        terminalSettingsForm.terminalSettingsState = terminalSettings.state
    }

    override fun disposeUIResources() { }
}