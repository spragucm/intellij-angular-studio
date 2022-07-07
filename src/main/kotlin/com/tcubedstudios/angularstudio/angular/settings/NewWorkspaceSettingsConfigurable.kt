package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.ide.projectWizard.NewProjectWizardCollector
import com.intellij.openapi.Disposable
import javax.swing.JComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.observable.util.toUiPathProperty
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.util.Alarm
import com.tcubedstudios.angularstudio.shared.util.checkBoxRow
import com.tcubedstudios.angularstudio.shared.util.textRow
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel
import javax.swing.SwingUtilities

class NewWorkspaceSettingsConfigurable: Configurable {

    companion object {
        private const val COLUMN1 = "column1"
        private const val COLUMN2 = "column2"
    }

    private var newWorkspaceSettings = NewWorkspaceSettings.getInstance()
    //private var newWorkspaceSettingsState = newWorkspaceSettings.state

    private val propertyGraph = PropertyGraph()

    private val workspaceNameProperty: ObservableMutableProperty<String> = propertyGraph.property("alsdkjflaksdjf ")//newWorkspaceSettings.state.workspaceName
    private var workspaceName: String by workspaceNameProperty

    private val workspaceTemplatePathProperty: GraphProperty<String> = propertyGraph.property("")
    private var workspaceTemplatePath: String by workspaceTemplatePathProperty

    private val workspacePathProperty: GraphProperty<String> = propertyGraph.property("a:/")
    private var workspacePath: String by workspacePathProperty

    private val prefixProperty: GraphProperty<String> = propertyGraph.property("b:/")
    private val prefixProperty2: GraphProperty<String> = propertyGraph.property("b:2/")
    private var prefix: String by prefixProperty

    private val collectionProperty: GraphProperty<String> = propertyGraph.property("c:/")
    private var collection: String by collectionProperty

    private val newProjectRootProperty: GraphProperty<String> = propertyGraph.property("d:/")
    private var newProjectRoot: String by newProjectRootProperty

    private val directoryNameProperty: GraphProperty<String> = propertyGraph.property("e:/")
    private var directoryName: String by directoryNameProperty

    private var sourceRootPath: String = ""
    private lateinit var pathField: TextFieldWithBrowseButton

    val alarm = Alarm {}

    private lateinit var panel: DialogPanel
    private fun initValidation() {
        alarm.addRequest(Runnable {
            panel.apply()
            val modified = panel.isModified()
            initValidation()
        }, 5000)
    }

//    private var selectedDirectory: VirtualFile? = VirtualFileManager.getInstance().findFileByUrl("c:")
    private val model = Model()
    /*init {
        newWorkspaceSettingsForm.workspacePathButton?.addActionListener {
            val projectManager = ProjectManager.getInstance()
            val project = projectManager.defaultProject
            val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
            val selectedDirectory = FileChooserDialogImpl(fileChooserDescriptor, project).choose(project, selectedDirectory)
        }
    }*/

    init {
//        workspaceName.afterChange {}
    }

    override fun getDisplayName(): String  = "New Angular Workspace Settings"

    override fun getHelpTopic(): String = "Configure New Angular Workspace Settings"

    override fun createComponent(): JComponent? {
        //TODO - CHRIS - should this include the idea of "use template", "if not in template, value is"

//        browserLink(IdeBundle.message("date.format.date.patterns"), "https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html"
        panel = panel {
            group {
//                textRow("Prefix2", prefixProperty2) { str ->
//                    println(str.get())
//                    true
//                }
                row("Workspace Name12") {
                    textField()
                        .bindText(NewWorkspaceSettings.getInstance().state::workspaceName)//
//                        .apply { commentProperty.afterChange { comment?.text = it } }
//                        .whenTextChangedFromUi {}
//                        bla.onIsModified {
//                            newWorkspaceSettings.state.workspaceName = "Anything"
//                            return@onIsModified true
//                        }
//                        bla.onApply {
//                            val blabla = "anything"
//                        }
//                        bla.validationOnApply {
//                            val blablabla = "anojisdf"
//                        }
//                        .label(GitBundle.message("new.branch.dialog.branch.name"), LabelPosition.TOP)
                        /*.bindText(model::serverUri)
                        .horizontalAlign(HorizontalAlign.FILL)
                        .resizableColumn()
                        .comment(tokenNote)
                        .enabledIf(progressModel.toComponentPredicate(!serverFieldDisabled))
                        .validationOnApply {*/
//                        .trimmedTextValidation(CHECK_NON_EMPTY, CHECK_GROUP_ID)
//                        .withSpecialValidation(CHECK_NOT_EMPTY, CHECK_NO_WHITESPACES, CHECK_NO_RESERVED_WORDS, CHECK_PACKAGE_NAME)
//                        .validationOnInput { validateModuleName() }
//                        .validationOnApply { validateModuleName() }
//                        .whenTextChangedFromUi { NewProjectWizardCollector.BuildSystem.logModuleNameChanged() }
                        .horizontalAlign(HorizontalAlign.FILL)
                }

                /*textRow("Workspace Name", workspaceNameProperty) {
                    newWorkspaceSettings.state.workspaceName = it.get()
                    return@textRow true
                }*/
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
//                textRow("Prefix", prefixProperty)

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
                textRow("New ProjectRoot", model::text2) { str ->
                    println(str.get())
                    true
                }
                row("Package Manager") {
                    comboBox(getPackageManagers(), SimpleListCellRenderer.create("npm") { it })
                        .horizontalAlign(HorizontalAlign.FILL)
                }
                textRow("Directory Name", directoryNameProperty) { str ->
                    println(str.get())
                    true
                }
                checkBoxRow("Skip Git")
                checkBoxRow("Skip Tests")
                checkBoxRow("Clean Install")
            }
            collapsibleGroup("Obscure Settings") {
                checkBoxRow("Defaults")
                checkBoxRow("Dry Run")
                textRow("Collection", collectionProperty)
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

        //SwingUtilities.invokeLater() {
            initValidation()
        //}

        return panel
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
//        newWorkspaceSettings.loadState(newWorkspaceSettingsState)
    }

    override fun reset() {
//        newWorkspaceSettingsForm.newWorkspaceSettingsState = newWorkspaceSettings.state
    }
}

data class Model (
    var text1: String = "",
    var text2: String = ""
)