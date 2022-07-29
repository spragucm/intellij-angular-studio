package com.tcubedstudios.angularstudio.angular.settings

import javax.swing.JComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
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
            return NewWorkspaceSettings.getInstance()
        }

    private val state: NewWorkspaceSettingsState
        get() {
            return settings.state
        }

    //TODO - CHRIS - this disposable needs a parent
    //Maybe this is useful: createExtensionDisposable()
    val alarm = Alarm {}

    private lateinit var panel: DialogPanel

    //TODO - CHRIS - what is this being used for?
    //I think that it causes the the fields to be bound to settings so that the fields
    //don't have to wait on some other triggers (ie it's auto saving every 5 secs)
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
                textRow("Workspace Name", state::workspaceName)
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
                checkBoxRow(state::force, "Force")
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
                        .bindText(state::workspacePath)//.toUiPathProperty()
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
                checkBoxRow(state::replaceEntireJsonWithTemplate, "Replace entire package.json with template")
                checkBoxRow(state::createWorkspaceInWorkingDirectory, "Create Worksapce InWorkingDirectory")
                checkBoxRow(state::isMonoRepo, "Is Mono Repo")
                textRow("Prefix", state::prefix)
                checkBoxRow(state::routing, "Routing")
                checkBoxRow(state::strict, "Strict")
                comboBoxRow("Style", state::style, Style::class.java)
            }
            collapsibleGroup("Advanced Settings") {
                checkBoxRow(state::inlineStyle, "Inline Style")
                checkBoxRow(state::inlineTemplate, "Inline Template")
                textRow("New ProjectRoot", state::newProjectRoot)
                comboBoxRow("Package Manager", state::packageManager, PackageManager::class.java)
                textRow("Directory Name", state::directoryName)
                checkBoxRow(state::skipGit, "Skip Git")
                checkBoxRow(state::skipTests, "Skip Tests")
                checkBoxRow(state::cleanInstall, "Clean Install")
            }
            collapsibleGroup("Obscure Settings") {
                checkBoxRow(state::defaults, "Defaults")
                checkBoxRow(state::dryRun, "Dry Run")
                textRow("Collection", state::collection)
                checkBoxRow(state::commit, "Commit")
                checkBoxRow(state::interactive, "Interactive")
                checkBoxRow(state::minimal, "Minimal")
                checkBoxRow(state::skipInstall, "Skip Install")
                checkBoxRow(state::verbose, "Verbose")
                comboBoxRow("View Encapsulation", state::viewEncapsulation, ViewEncapsulation::class.java)
                checkBoxRow(state::showCriticalDialogs, "Show Critical Dialogs")
                checkBoxRow(state::showStandardDialogs, "Show Standard Dialogs")
                checkBoxRow(state::showAdvancedDialogs, "Show Advanced Dialogs")
                checkBoxRow(state::showObscureDialogs, "Show Obscure Dialogs")
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

    override fun apply() {}

    override fun reset() {}
}