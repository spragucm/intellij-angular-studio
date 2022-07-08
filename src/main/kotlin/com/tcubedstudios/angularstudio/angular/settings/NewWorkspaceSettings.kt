package com.tcubedstudios.angularstudio.angular.settings

import com.intellij.openapi.components.*
import com.tcubedstudios.angularstudio.angular.terminal.args.PackageManager
import com.tcubedstudios.angularstudio.angular.terminal.args.PackageManager.NPM
import com.tcubedstudios.angularstudio.angular.terminal.args.Style
import com.tcubedstudios.angularstudio.angular.terminal.args.Style.SCSS
import com.tcubedstudios.angularstudio.angular.terminal.args.ViewEncapsulation
import com.tcubedstudios.angularstudio.angular.terminal.args.ViewEncapsulation.EMULATED

@State(
    name = "NewWorkspaceSettings",
    storages = [Storage("newWorkspaceSettings.xml")]
)
class NewWorkspaceSettings: PersistentStateComponent<NewWorkspaceSettings> {//NewWorkspaceSettingsState

    //checkbox to show single keyword for ng, or show show?
    var workspaceName: String = "" // never retain, this must be unique
    var force: Boolean = false
    var workspaceTemplate: String = ""//TODO - Chris - This is actually a spinner with dynamic value
    var workspacePath: String = ""
    var workspacePackageJsonTemplatePath: String = ""
    var replaceEntireJsonWithTemplate: Boolean = false
    var createWorkspaceInWorkingDirectory: Boolean = false
    var isMonoRepo: Boolean = true
    var prefix: String = "app"//anyting but empty
    var routing: Boolean = true
    var strict: Boolean = true
    var style: Style = SCSS
    var inlineStyle: Boolean = false
    var inlineTemplate: Boolean = false
    var newProjectRoot: String = "projects"//anything but empty
    var packageManager: PackageManager = NPM
    var directoryName: String = ""
    var skipGit: Boolean = false
    var skipTests: Boolean = false
    var cleanInstall: Boolean = false
    var defaults: Boolean = false
    var dryRun: Boolean = false
    var collection: String = ""
    var commit: Boolean = true
    var interactive: Boolean = false
    var minimal: Boolean = false
    var skipInstall: Boolean = false
    var verbose: Boolean = false
    var viewEncapsulation: ViewEncapsulation = EMULATED
    var showCriticalDialogs: Boolean = true
    var showStandardDialogs: Boolean = false
    var showAdvancedDialogs: Boolean = false
    var showObscureDialogs: Boolean = false

    companion object {
        fun getInstance(): NewWorkspaceSettings = ServiceManager.getService(NewWorkspaceSettings::class.java)
    }

//    private var newWorkspaceSettingsState = NewWorkspaceSettingsState()

    override fun getState(): NewWorkspaceSettings = this//newWorkspaceSettingsState

    override fun loadState(newWorkspaceSettings: NewWorkspaceSettings) {
        this.workspaceName = newWorkspaceSettings.workspaceName
        this.force = newWorkspaceSettings.force
        this.workspaceTemplate = newWorkspaceSettings.workspaceTemplate
        this.workspacePath = newWorkspaceSettings.workspacePath
        this.workspacePackageJsonTemplatePath = newWorkspaceSettings.workspacePackageJsonTemplatePath
        this.replaceEntireJsonWithTemplate = newWorkspaceSettings.replaceEntireJsonWithTemplate
        this.createWorkspaceInWorkingDirectory = newWorkspaceSettings.createWorkspaceInWorkingDirectory
        this.isMonoRepo = newWorkspaceSettings.isMonoRepo
        this.prefix = newWorkspaceSettings.prefix
        this.routing = newWorkspaceSettings.routing
        this.strict = newWorkspaceSettings.strict
        this.style = newWorkspaceSettings.style
        this.inlineStyle = newWorkspaceSettings.inlineStyle
        this.inlineTemplate = newWorkspaceSettings.inlineTemplate
        this.newProjectRoot = newWorkspaceSettings.newProjectRoot
        this.packageManager = newWorkspaceSettings.packageManager
        this.directoryName = newWorkspaceSettings.directoryName
        this.skipGit = newWorkspaceSettings.skipGit
        this.skipTests = newWorkspaceSettings.skipTests
        this.cleanInstall = newWorkspaceSettings.cleanInstall
        this.defaults = newWorkspaceSettings.defaults
        this.dryRun = newWorkspaceSettings.dryRun
        this.collection = newWorkspaceSettings.collection
        this.commit = newWorkspaceSettings.commit
        this.interactive = newWorkspaceSettings.interactive
        this.minimal = newWorkspaceSettings.minimal
        this.skipInstall = newWorkspaceSettings.skipInstall
        this.verbose = newWorkspaceSettings.verbose
        this.viewEncapsulation = newWorkspaceSettings.viewEncapsulation
        this.showCriticalDialogs = newWorkspaceSettings.showCriticalDialogs
        this.showStandardDialogs = newWorkspaceSettings.showStandardDialogs
        this.showAdvancedDialogs = newWorkspaceSettings.showAdvancedDialogs
        this.showObscureDialogs = newWorkspaceSettings.showObscureDialogs
        //this.newWorkspaceSettingsState = newWorkspaceSettingsState
    }
}