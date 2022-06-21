package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAwareAction
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException


// https://intellij-support.jetbrains.com/hc/en-us/community/posts/360005329339-Execute-command-in-the-terminal-from-plugin-action?page=1#community_comment_6186561275794
class RunCommandInLocalTerminalAction: DumbAwareAction() {//BaseAction in example
//  Get a tab of our choice
//  we want to define this because otherwise it will take too much time to execute each command
// since a new terminal and then loading files, takes forever
//    var content: Content = contentManager.findContent("tab name")
//    var widget = TerminalView.getWidgetByContent(content)
    companion object {
        val LOGGY = Logger.getInstance(RunCommandInLocalTerminalAction::class.java)
    }

    override fun update(event: AnActionEvent) {
        val project = event.getEventProject()
        event.presentation.isEnabledAndVisible = true//project != null
    }

    override fun actionPerformed(event: AnActionEvent) {
         //should this be getEventProject?
        when (val project = event.project) {
            null -> LOGGY.error("Cannot run command in local terminal. Project is null")
            else -> {
                try {
                    val terminalView = TerminalView.getInstance(project)
                    terminalView.createLocalShellWidget(project.basePath, "Name").executeCommand("ng help")
                } catch (e: IOException) {
                    LOGGY.error("Cannot run command in local terminal. Error:$e")
                }
            }
        }
    }
}