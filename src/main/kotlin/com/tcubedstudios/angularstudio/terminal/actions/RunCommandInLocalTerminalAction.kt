package com.tcubedstudios.angularstudio.terminal.actions

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.ToolWindowManager
import com.tcubedstudios.angularstudio.terminal.utils.getEventProject
import com.tcubedstudios.angularstudio.util.FileUtils
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

// https://intellij-support.jetbrains.com/hc/en-us/community/posts/360005329339-Execute-command-in-the-terminal-from-plugin-action?page=1#community_comment_6186561275794
// https://github.com/JetBrains/intellij-community/blob/95ab6a1ecfdf49754f7eb5a81984cdc2c4fa0ca5/plugins/sh/src/com/intellij/sh/run/ShTerminalRunner.java
class RunCommandInLocalTerminalAction: DumbAwareAction() {
    companion object {
        val LOGGY = Logger.getInstance(RunCommandInLocalTerminalAction::class.java)
        val TAB_NAME = "Angular Studio"
    }

    override fun update(event: AnActionEvent) {
        val project = event.getEventProject()
        event.presentation.isEnabledAndVisible = true//project != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        when (val project = event.getEventProject()) {
            null -> LOGGY.error("Cannot run command in local terminal. Project is null")
            else -> {
                try {
                    val fileInputStream = FileUtils.getFileInputStream("/scripts/DeleteMe.txt")
                    println("file:$fileInputStream")

                    //val file = File("/com/tcubedstudios/angularstudio/resources/DeleteMe.txt")///src/main/kotlin/com.tcubedstudios.angularstudio/scripts/DeleteMe.txt
//                    val fileType: FileType = FileTypeManager.getInstance().getFileTypeByFileName("pom.xml")
                    //val content: String = Files.asCharSource(file, Charsets.UTF_8).read()
                    //println(content)




                    val pluginPath = PluginManager.getPlugin(PluginId.getId("com.tcubedstudios.angularstudio"))?.path
                    val scriptsRootPath = "$pluginPath\\scripts" //"I:\\Google Drive (HoverDroids)\\Code\\Windows\\Scripts"
                    val scriptPath = "$scriptsRootPath\\Angular\\CreateWorkspaceWithMultipleProjectsAndLibraries.ps1"
                    val scriptArguments = "-skipInstall true"

                    val terminalView = TerminalView.getInstance(project)
                    val window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)

                    val contentManager = window?.contentManager
                    val terminalWorkingDirectory = project.basePath

                    val widget = when (val content = contentManager?.findContent(TAB_NAME)) {
                        null -> terminalView.createLocalShellWidget(terminalWorkingDirectory, TAB_NAME)
                        else -> TerminalView.getWidgetByContent(content) as ShellTerminalWidget
                    }
                    val cmd =  "& \"$scriptPath\" $scriptArguments"
                    widget.executeCommand(cmd)

                } catch (e: IOException) {
                    LOGGY.error("Cannot run command in local terminal. Error:$e")
                }
            }
        }
    }
}