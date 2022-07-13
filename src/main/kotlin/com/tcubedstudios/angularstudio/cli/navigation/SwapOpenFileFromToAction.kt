package com.tcubedstudios.angularstudio.cli.navigation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.vfs.VirtualFile
import com.tcubedstudios.angularstudio.shared.util.FileUtils.getNextExtensionForFileName
import com.tcubedstudios.angularstudio.shared.util.FileUtils.swapOpenFileFromTo

class SwapOpenFileFromToAction: AnAction("QuickSwitch") {
    //TODO - CHRIS - this should be a selectable list in settings
    companion object {
        val extensions = listOf("ts", "js", "html", "php", "haml", "jade", "pug", "slim", "css", "sass", "scss", "less", "style", "styl")
    }

    private lateinit var latestEvent: AnActionEvent

    override fun actionPerformed(event: AnActionEvent) {
        latestEvent = event

        val currentFile = event.dataContext.getData(VIRTUAL_FILE) ?: return
        val currentFilePath = currentFile.canonicalPath ?: return
        val directoryPath = currentFile.parent.canonicalPath ?: return
        val fromExtension = currentFile.extension ?: return
        val fileName = currentFile.nameWithoutExtension
        val project = latestEvent.project ?: return

        val toExtension = getNextExtensionForFileName(directoryPath, fileName, fromExtension, extensions)
        swapOpenFileFromTo(project, directoryPath, fileName, fromExtension, toExtension)
    }
}