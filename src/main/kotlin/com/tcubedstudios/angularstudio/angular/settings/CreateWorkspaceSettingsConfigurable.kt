package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import javax.swing.JComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.observable.util.toUiPathProperty
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.tcubedstudios.angularstudio.shared.util.checkBoxRow
import com.tcubedstudios.angularstudio.shared.util.textRow
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel

class CreateWorkspaceSettingsConfigurable: Configurable {

    companion object {
        private const val COLUMN1 = "column1"
        private const val COLUMN2 = "column2"
    }

    private val propertyGraph = PropertyGraph()

    private val workspaceNameProperty: GraphProperty<String> = propertyGraph.property("")
    private var workspaceName: String by workspaceNameProperty

    private val workspaceTemplatePathProperty: GraphProperty<String> = propertyGraph.property("")
    private var workspaceTemplatePath: String by workspaceTemplatePathProperty

    private val workspacePathProperty: GraphProperty<String> = propertyGraph.property("c:/")
    private var workspacePath: String by workspacePathProperty


    private var sourceRootPath: String = ""
    private lateinit var pathField: TextFieldWithBrowseButton

//    private var selectedDirectory: VirtualFile? = VirtualFileManager.getInstance().findFileByUrl("c:")

    /*init {
        newWorkspaceSettingsForm.workspacePathButton?.addActionListener {
            val projectManager = ProjectManager.getInstance()
            val project = projectManager.defaultProject
            val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
            val selectedDirectory = FileChooserDialogImpl(fileChooserDescriptor, project).choose(project, selectedDirectory)
        }
    }*/

    override fun getDisplayName(): String  = "New Angular Workspace Settings"

    override fun getHelpTopic(): String = "Configure New Angular Workspace Settings"

    override fun createComponent(): JComponent? {
        //TODO - CHRIS - should this include the idea of "use template", "if not in template, value is"
        return panel {
            group {
                textRow("Workspace Name")//.bindText(workspaceNameProperty)
                checkBoxRow("Force")
            }
            collapsibleGroup("Standard Settings") {
                row("Workspace Template") {
                    comboBox(getWorkspaceTemplates(), SimpleListCellRenderer.create("") { it.first })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
                row("Workspace Path") {
                    textFieldWithBrowseButton(
                        null,
                        null,
                        FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                        null
                    )
                        .horizontalAlign(HorizontalAlign.FILL)
                        .bindText(workspacePathProperty.toUiPathProperty())
//                        .onIsModified {
//                            //TODO - CHRIS
//                            return@onIsModified true
//                        }
//                        .widthGroup(COLUMN1)

                }
                row("Workspace package.json Path") {
                    comboBox(getPackageJsonTemplates(), SimpleListCellRenderer.create("") { it.first })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
                checkBoxRow("Replace entire package.json with template")
                checkBoxRow("Create Worksapce InWorkingDirectory")
                checkBoxRow("Is Mono Repo")
                textRow("Prefix")
                checkBoxRow("Routing")
                checkBoxRow("Strict")
                row("Style") {
                    comboBox(getStyles(), SimpleListCellRenderer.create("scss") { it })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
            }
            collapsibleGroup("Advanced Settings") {
                checkBoxRow("Inline Style")
                checkBoxRow("Inline Template")
                textRow("New ProjectRoot")
                row("Package Manager") {
                    comboBox(getPackageManagers(), SimpleListCellRenderer.create("npm") { it })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
                textRow("Directory Name")
                checkBoxRow("Skip Git")
                checkBoxRow("Skip Tests")
                checkBoxRow("Clean Install")
            }
            collapsibleGroup("Obscure Settings") {
                checkBoxRow("Defaults")
                checkBoxRow("Dry Run")
                textRow("Collection")
                checkBoxRow("Commit")
                checkBoxRow("Interactive")
                checkBoxRow("Minimal")
                checkBoxRow("Skip Install")
                checkBoxRow("Verbose")
                row("View Encapsulation") {
                    comboBox(getViewEncapsulations(), SimpleListCellRenderer.create("Emulated") { it })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
                checkBoxRow("Show Critical Dialogs")
                checkBoxRow("Show Standard Dialogs")
                checkBoxRow("Show Advanced Dialogs")
                checkBoxRow("Show Obscure Dialogs")

            }.bottomGap(BottomGap.SMALL)
        }
    }

    private fun getWorkspaceTemplates(): ComboBoxModel<Pair<String, String>> {
        // TODO - CHRIS - programatically determine the template name and path
        val workspaceTemplatePairs = listOf(
            Pair("None", ""),
            Pair("Mono Repo Angular 13.2.2", "/scripts/Angular/Templates/Workspace/MonoRepoAngular13.2.2.json")
        )

        return DefaultComboBoxModel<Pair<String,String>>().apply {
            addAll(workspaceTemplatePairs)
        }
    }

    private fun getPackageJsonTemplates(): ComboBoxModel<Pair<String, String>> {
        // TODO - CHRIS programatically determine the package.json name and path
        val packageJsonPairs = listOf(
            Pair("None", ""),
            Pair("App : Angular 13.2.2", "/scripts/Angular/Templates/Package_Json/app-angular.13.2.2-package.json"),
            Pair("App : Angular 13.2.2 : Ionic 6.0.3", "/scripts/Angular/Templates/Package_Json/app-angular.13.2.2-ionic.6.0.3-electron-capacitor-package.json")
        )

        return DefaultComboBoxModel<Pair<String,String>>().apply {
            addAll(packageJsonPairs)
        }
    }

    private fun getPackageManagers(): ComboBoxModel<String> {
        // TODO - CHRIS - this could probably be an enum, but there could also eventually be any number of package getPackageManagers
        val packageManagers = listOf("npm")

        return DefaultComboBoxModel<String>().apply {
            addAll(packageManagers)
        }
    }

    private fun getStyles(): ComboBoxModel<String> {
        //TODO - CHRIS - this can probably be an enum
        val styles = listOf("scss")

        return DefaultComboBoxModel<String>().apply {
            addAll(styles)
        }
    }

    private fun getViewEncapsulations(): ComboBoxModel<String> {
        //TODO - CHRIS - this can be an enum,
        val encapsulations = listOf("Emulated")

        return DefaultComboBoxModel<String>().apply {
            addAll(encapsulations)
        }
    }

    override fun isModified(): Boolean= false

    override fun apply() {
//        newWorkspaceSettings.loadState(newWorkspaceSettingsForm.newWorkspaceSettingsState)
    }

    override fun reset() {
//        newWorkspaceSettingsForm.newWorkspaceSettingsState = newWorkspaceSettings.state
    }
}