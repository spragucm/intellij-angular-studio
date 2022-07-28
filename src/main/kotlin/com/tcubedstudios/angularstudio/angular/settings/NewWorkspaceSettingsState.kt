package com.tcubedstudios.angularstudio.angular.settings

import com.tcubedstudios.angularstudio.angular.terminal.args.PackageManager
import com.tcubedstudios.angularstudio.angular.terminal.args.Style
import com.tcubedstudios.angularstudio.angular.terminal.args.ViewEncapsulation

// DO NOT USE A DATA CLASS!
// There must be a default constructor without args that defines the initial state when no settings file can be found
// Reference: https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html?from=jetbrains.org#implementing-the-persistentstatecomponent-interface
class NewWorkspaceSettingsState {
    //checkbox to show single keyword for ng or show show?
    var workspaceName: String = "" // never retain this must be unique
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
    var style: Style = Style.SCSS
    var inlineStyle: Boolean = false
    var inlineTemplate: Boolean = false
    var newProjectRoot: String = "projects"//anything but empty
    var packageManager: PackageManager = PackageManager.NPM
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
    var viewEncapsulation: ViewEncapsulation = ViewEncapsulation.EMULATED
    var showCriticalDialogs: Boolean = true
    var showStandardDialogs: Boolean = false
    var showAdvancedDialogs: Boolean = false
    var showObscureDialogs: Boolean = false
}