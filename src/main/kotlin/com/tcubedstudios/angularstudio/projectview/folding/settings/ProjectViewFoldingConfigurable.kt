package com.tcubedstudios.angularstudio.projectview.folding.settings

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.impl.AbstractProjectTreeStructure
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.Alarm
import javax.swing.JComponent
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.openapi.project.Project
import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.util.ui.tree.TreeUtil
import javax.swing.BorderFactory.createEmptyBorder
import com.intellij.ui.layout.*
import com.tcubedstudios.angularstudio.MyBundle.message
import com.tcubedstudios.angularstudio.shared.utils.checkBoxRow
import javax.swing.JTextField
import javax.swing.event.DocumentEvent

class ProjectViewFoldingConfigurable(private val project: Project): SearchableConfigurable {

    companion object {
        //TODO - CHRIS - what's a good depth?
        const val TREE_EXPAND_DEPTH = 2
    }

    override fun getId() = "angularstudio.projectviewfolding"

    override fun getDisplayName() = "Project View Folding Settings"

    override fun isModified() = settingsInputComponent.isModified()

    private val settings: ProjectViewFoldingSettings
        get() {
            return ProjectViewFoldingSettings.getInstance()
        }

    private val state: ProjectViewFoldingSettingsState
        get() {
            return settings.state
        }

    private lateinit var settingsContainerComponent: JComponent
    private lateinit var settingsInputComponent: DialogPanel
    private lateinit var projectViewPane: ProjectViewPane
    private lateinit var projectViewPaneComponent: JComponent

    private val alarm = Alarm {}

    private lateinit var foldingEnabledPredicate: ComponentPredicate

    override fun apply() {
        val updated = isModified
        settingsInputComponent.apply()
        if (updated) {
            // Notify the ProjectViewFoldingTreeStructureProvider that settings have changed
            // so that it can refresh the tree
            ApplicationManager.getApplication()
                .messageBus
                .syncPublisher(IProjectViewFoldingSettingsListener.TOPIC)
                .settingsChanged(settings)
        }
    }

    override fun reset() {
        settingsInputComponent.reset()
    }

    private fun addDelayedSaveSettingsRequest() {
        alarm.addRequest(Runnable {
            apply()
            addDelayedSaveSettingsRequest()
        }, 5000)
    }

    override fun createComponent(): JComponent? {
        createSettingsInputPanel()
        createProjectViewPane()
        createSettingsPanel()

        TreeUtil.promiseExpand(projectViewPane.tree, TREE_EXPAND_DEPTH)
        addDelayedSaveSettingsRequest()
        return settingsContainerComponent
    }

    private fun createSettingsInputPanel() {
        settingsInputComponent = panel {
            group {
                checkBoxRow(
                    text = message("projectViewFolding.settings.foldingEnabled"),
                    property = state::foldingEnabled,
                    comment = message("projectViewFolding.settings.foldingEnabled.comment"),
                    mnemonic = 'e',
                    componentPredicate = { foldingEnabledPredicate = it }
                )
                checkBoxRow(
                    text = message("projectViewFolding.settings.foldDirectories"),
                    property = state::foldDirectories,
                    comment = message("projectViewFolding.settings.foldDirectories.comment"),
                    enableIf = foldingEnabledPredicate,
                    mnemonic ='d'
                )
                checkBoxRow(
                    text = message("projectViewFolding.settings.foldIgnoredFiles"),
                    property = state::hideIgnoredFiles,
                    comment = message("projectViewFolding.settings.foldIgnoredFiles.comment"),
                    enableIf = foldingEnabledPredicate,
                    mnemonic = 'h'
                )
                checkBoxRow(
                    text = message("projectViewFolding.settings.hideEmptyGroups"),
                    property = state::hideEmptyGroups,
                    comment = message("projectViewFolding.settings.hideEmptyGroups.comment"),
                    enableIf = foldingEnabledPredicate,
                    mnemonic = 'h'
                )
                    /*.apply {
                    // TODO - CHRIS
                    // hideAllGroupsPropert.afterPropagation { enabled = !hideAllGroupsProperty.get() }
                }*/
                checkBoxRow(
                    text = message("projectViewFolding.settings.hideAllGroups"),
                    property = state::hideAllGroups,
                    comment = message("projectViewFolding.settings.hideAllGroups.comment"),
                    enableIf = foldingEnabledPredicate,
                    mnemonic = 'i',
                    helpTitle = message("projectViewFolding.settings.hideAllGroups.help"),
                    helpDescription = message("projectViewFolding.settings.hideAllGroups.help.description")
                )

                checkBoxRow(
                    text = message("projectViewFolding.settings.caseInsensitive"),
                    property = state::caseInsensitive,
                    comment = message("projectViewFolding.settings.caseInsensitive.comment"),
                    enableIf = foldingEnabledPredicate,
                    mnemonic = 'c'
                )
            }
            group(message("projectViewFolding.settings.foldingRules"))  {
                row {
                    expandableTextField()
                        .label("Expandable text")
                        .applyToComponent {
                            text = "some tset alksdjflak sjdfla jskdjfalksdjfas jdkfja"
                            /*document.addDocumentListener(object: DocumentAdapter() {
                                override fun textChanged(e: DocumentEvent) {
                                    settings.ADDITIONAL_TAGS = text.trim()
                                }
                            })*/
                        }
                }
            }

        }.apply {
            border = createEmptyBorder(10, 10, 10, 30)
        }
    }

    private fun createProjectViewPane() {
        projectViewPane = object : ProjectViewPane(project) {
            override fun enableDnD() = Unit

            override fun createStructure() = object : AbstractProjectTreeStructure(project) {
                override fun getProviders() = listOf<TreeStructureProvider>()//TODO
            }
        }

        projectViewPaneComponent = projectViewPane.createComponent().apply {
            border = createEmptyBorder()
        }
    }

    private fun createSettingsPanel() {
        settingsContainerComponent = OnePixelSplitter(.3f).apply {
            firstComponent = settingsInputComponent
            secondComponent = projectViewPaneComponent
        }
    }
}