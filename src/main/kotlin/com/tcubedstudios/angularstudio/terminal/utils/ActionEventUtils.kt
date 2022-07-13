package com.tcubedstudios.angularstudio.terminal.utils

import com.intellij.ide.actions.RevealFileAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

fun AnActionEvent?.getSelectedDirectory(): VirtualFile? {
    val file = getSelectedFile()
    return when {
        file == null -> null
        file.isDirectory -> file
        else -> file.parent
    }
}

fun AnActionEvent?.getSelectedFile(): VirtualFile? {
    return when (this) {
        null -> null
        else -> RevealFileAction.findLocalFile(getData(VIRTUAL_FILE))
    }
}

fun AnActionEvent?.getEventProject(): Project? = AnAction.getEventProject(this)