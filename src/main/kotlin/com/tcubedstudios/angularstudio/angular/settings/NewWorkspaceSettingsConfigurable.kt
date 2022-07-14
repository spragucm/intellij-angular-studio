package com.tcubedstudios.angularstudio.angular.settings

import javax.swing.JComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.util.Alarm
import com.tcubedstudios.angularstudio.angular.terminal.args.PackageManager
import com.tcubedstudios.angularstudio.angular.terminal.args.Style
import com.tcubedstudios.angularstudio.angular.terminal.args.ViewEncapsulation
import com.tcubedstudios.angularstudio.shared.utils.checkBoxRow
import com.tcubedstudios.angularstudio.shared.utils.comboBoxRow
import com.tcubedstudios.angularstudio.shared.utils.textRow
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel

class NewWorkspaceSettingsConfigurable: Configurable {

    companion object {
        private const val COLUMN1 = "column1"
        private const val COLUMN2 = "column2"
    }

    private val settings: NewWorkspaceSettings
        get() {
            return NewWorkspaceSettings.getInstance().state
        }

    private var newWorkspaceSettings = NewWorkspaceSettings.getInstance()
    //private var newWorkspaceSettingsState = newWorkspaceSettings.state

    private val propertyGraph = PropertyGraph()

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
                textRow("Workspace Name", settings::workspaceName)
//                row("Workspace Name12") {
//                    textField()
//                        .bindText(settings::workspaceName)//
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
//                        .horizontalAlign(HorizontalAlign.FILL)
//                }

                /*textRow("Workspace Name", workspaceNameProperty) {
                    newWorkspaceSettings.state.workspaceName = it.get()
                    return@textRow true
                }*/
                checkBoxRow("Force", settings::force)
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
                        .bindText(settings::workspacePath)//.toUiPathProperty()
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
                checkBoxRow("Replace entire package.json with template", settings::replaceEntireJsonWithTemplate)
                checkBoxRow("Create Worksapce InWorkingDirectory", settings::createWorkspaceInWorkingDirectory)
                checkBoxRow("Is Mono Repo", settings::isMonoRepo)
                textRow("Prefix", settings::prefix)
                checkBoxRow("Routing", settings::routing)
                checkBoxRow("Strict", settings::strict)
                comboBoxRow("Style", settings::style, Style::class.java)
            }
            collapsibleGroup("Advanced Settings") {
                checkBoxRow("Inline Style", settings::inlineStyle)
                checkBoxRow("Inline Template", settings::inlineTemplate)
                textRow("New ProjectRoot", settings::newProjectRoot)
                comboBoxRow("Package Manager", settings::packageManager, PackageManager::class.java)
                textRow("Directory Name", settings::directoryName)
                checkBoxRow("Skip Git", settings::skipGit)
                checkBoxRow("Skip Tests", settings::skipTests)
                checkBoxRow("Clean Install", settings::cleanInstall)
            }
            collapsibleGroup("Obscure Settings") {
                checkBoxRow("Defaults", settings::defaults)
                checkBoxRow("Dry Run", settings::dryRun)
                textRow("Collection", settings::collection)
                checkBoxRow("Commit", settings::commit)
                checkBoxRow("Interactive", settings::interactive)
                checkBoxRow("Minimal", settings::minimal)
                checkBoxRow("Skip Install", settings::skipInstall)
                checkBoxRow("Verbose", settings::verbose)
                comboBoxRow("View Encapsulation", settings::viewEncapsulation, ViewEncapsulation::class.java)
                checkBoxRow("Show Critical Dialogs", settings::showCriticalDialogs)
                checkBoxRow("Show Standard Dialogs", settings::showStandardDialogs)
                checkBoxRow("Show Advanced Dialogs", settings::showAdvancedDialogs)
                checkBoxRow("Show Obscure Dialogs", settings::showObscureDialogs)
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