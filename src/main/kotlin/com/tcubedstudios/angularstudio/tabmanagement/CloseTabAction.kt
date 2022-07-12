package com.tcubedstudios.angularstudio.tabmanagement

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.tcubedstudios.angularstudio.shared.Direction

abstract class CloseTabAction (
    private val direction: Direction
): DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        CloseTabLeftRight(event, direction).closeFiles()
    }

    override fun update(event: AnActionEvent) {
        super.update(event)
        CloseTabLeftRight(event, direction).updatePresentationEnabled()
    }
}