package com.tcubedstudios.angularstudio.ide.tabmanagement.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.vfs.VirtualFile
import com.tcubedstudios.angularstudio.shared.simpletons.Direction
import com.tcubedstudios.angularstudio.shared.utils.FileUtils.files

class CloseTabLeftRightAction(
    private val event: AnActionEvent,
    private val direction: Direction,
    private val editorManager: FileEditorManagerEx = FileEditorManagerEx.getInstanceEx(event.project!!),
    private val files: List<VirtualFile> = editorManager.files(direction)
) {
    fun closeFiles() {
        files.forEach { editorManager.currentWindow.closeFile(it) }
    }

    fun updatePresentationEnabled() {
        event.presentation.isEnabled = files.isNotEmpty()
    }
}